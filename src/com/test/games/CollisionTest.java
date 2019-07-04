package com.test.games;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CollisionTest extends ShooterCore {

  public static void main(String[] args) {
    new CollisionTest(args).run();
  }

  protected BSPTree bspTree;

  protected String mapFile;

  public CollisionTest(String[] args) {
    super(args);
    for (int i = 0; mapFile == null && i < args.length; i++) {
      if (mapFile == null && !args[i].startsWith("-")) {
        mapFile = args[i];
      }
    }
    if (mapFile == null) {
      mapFile = "../images/sample.map";
    }
  }

  public void createPolygons() {
    Graphics2D g = screen.getGraphics();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    g.setColor(Color.WHITE);
    g.drawString("Loading...", 5, screen.getHeight() - 5);
    screen.update();

    float ambientLightIntensity = .2f;
    List lights = new LinkedList();
    lights.add(new PointLight3D(-100, 100, 100, .3f, -1));
    lights.add(new PointLight3D(100, 100, 0, .3f, -1));

    MapLoader loader = new MapLoader();
    loader.setObjectLights(lights, ambientLightIntensity);

    try {
      bspTree = loader.loadMap(mapFile);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    CollisionDetection collisionDetection = new CollisionDetection(bspTree);
    gameObjectManager = new GridGameObjectManager(bspTree.calcBounds(),
        collisionDetection);
    gameObjectManager.addPlayer(new GameObject(new PolygonGroup("Player")));

    // set up player bounds
    PolygonGroupBounds playerBounds = gameObjectManager.getPlayer()
        .getBounds();
    playerBounds.setTopHeight(Player.DEFAULT_PLAYER_HEIGHT);
    playerBounds.setRadius(Player.DEFAULT_PLAYER_RADIUS);

    ((BSPRenderer) polygonRenderer).setGameObjectManager(gameObjectManager);

    createGameObjects(loader.getObjectsInMap());
    Transform3D start = loader.getPlayerStartLocation();
    gameObjectManager.getPlayer().getTransform().setTo(start);
  }

  private void createGameObjects(List mapObjects) {
    Iterator i = mapObjects.iterator();
    while (i.hasNext()) {
      PolygonGroup group = (PolygonGroup) i.next();
      String filename = group.getFilename();
      if ("robot.obj".equals(filename)) {
        gameObjectManager.add(new Bot(group));
      } else {
        // static object
        gameObjectManager.add(new GameObject(group));
      }
    }
  }

  public void drawPolygons(Graphics2D g) {

    polygonRenderer.startFrame(g);

    // draw polygons in bsp tree (set z buffer)
    ((BSPRenderer) polygonRenderer).draw(g, bspTree);

    // draw game object polygons (check and set z buffer)
    gameObjectManager.draw(g, (GameObjectRenderer) polygonRenderer);

    polygonRenderer.endFrame(g);

  }
}

/**
 * A GameObject that can jump.
 */

class JumpingGameObject extends GameObject {

  public static final float DEFAULT_JUMP_HEIGHT = 64;

  protected float jumpVelocity;

  public JumpingGameObject(PolygonGroup group) {
    super(group);
    setJumpHeight(DEFAULT_JUMP_HEIGHT);
  }

  /**
   * Sets how high this GameObject can jump.
   */
  public void setJumpHeight(float jumpHeight) {
    jumpVelocity = Physics.getInstance().getJumpVelocity(jumpHeight);
  }

  /**
   * Causes this GameObject to jump if the jumping flag is set and this object
   * is not already jumping.
   */
  public void setJumping(boolean isJumping) {
    if (isJumping() != isJumping) {
      super.setJumping(isJumping);
      if (isJumping) {
        Physics.getInstance().jump(this, jumpVelocity);
      }
    }
  }

  public void notifyFloorCollision() {
    // the object has landed.
    setJumping(false);
  }

}
/**
 * The Physics class is a singleton that represents various attributes (like
 * gravity) and the functions to manipulate objects based on those physical
 * attributes. Currently, only gravity and scoot-up (acceleration when traveling
 * up stairs) are supported.
 */

class Physics {

  /**
   * Default gravity in units per millisecond squared
   */
  public static final float DEFAULT_GRAVITY_ACCEL = -.002f;

  /**
   * Default scoot-up (acceleration traveling up stairs) in units per
   * millisecond squared.
   */
  public static final float DEFAULT_SCOOT_ACCEL = .006f;

  private static Physics instance;

  private float gravityAccel;

  private float scootAccel;

  private Vector3D velocity = new Vector3D();

  /**
   * Gets the Physics instance. If a Physics instance does not yet exist, one
   * is created with the default attributes.
   */
  public static synchronized Physics getInstance() {
    if (instance == null) {
      instance = new Physics();
    }
    return instance;
  }

  protected Physics() {
    gravityAccel = DEFAULT_GRAVITY_ACCEL;
    scootAccel = DEFAULT_SCOOT_ACCEL;
  }

  /**
   * Gets the gravity acceleration in units per millisecond squared.
   */
  public float getGravityAccel() {
    return gravityAccel;
  }

  /**
   * Sets the gravity acceleration in units per millisecond squared.
   */
  public void setGravityAccel(float gravityAccel) {
    this.gravityAccel = gravityAccel;
  }

  /**
   * Gets the scoot-up acceleration in units per millisecond squared. The
   * scoot up acceleration can be used for smoothly traveling up stairs.
   */
  public float getScootAccel() {
    return scootAccel;
  }

  /**
   * Sets the scoot-up acceleration in units per millisecond squared. The
   * scoot up acceleration can be used for smoothly traveling up stairs.
   */
  public void setScootAccel(float scootAccel) {
    this.scootAccel = scootAccel;
  }

  /**
   * Applies gravity to the specified GameObject according to the amount of
   * time that has passed.
   */
  public void applyGravity(GameObject object, long elapsedTime) {
    velocity.setTo(0, gravityAccel * elapsedTime, 0);
    object.getTransform().addVelocity(velocity);
  }

  /**
   * Applies the scoot-up acceleration to the specified GameObject according
   * to the amount of time that has passed.
   */
  public void scootUp(GameObject object, long elapsedTime) {
    velocity.setTo(0, scootAccel * elapsedTime, 0);
    object.getTransform().addVelocity(velocity);
  }

  /**
   * Applies the negative scoot-up acceleration to the specified GameObject
   * according to the amount of time that has passed.
   */
  public void scootDown(GameObject object, long elapsedTime) {
    velocity.setTo(0, -scootAccel * elapsedTime, 0);
    object.getTransform().addVelocity(velocity);
  }

  /**
   * Sets the specified GameObject's vertical velocity to jump to the
   * specified height. Calls getJumpVelocity() to calculate the velocity,
   * which uses the Math.sqrt() function.
   */
  public void jumpToHeight(GameObject object, float jumpHeight) {
    jump(object, getJumpVelocity(jumpHeight));
  }

  /**
   * Sets the specified GameObject's vertical velocity to the specified jump
   * velocity.
   */
  public void jump(GameObject object, float jumpVelocity) {
    velocity.setTo(0, jumpVelocity, 0);
    object.getTransform().getVelocity().y = 0;
    object.getTransform().addVelocity(velocity);
  }

  /**
   * Returns the vertical velocity needed to jump the specified height (based
   * on current gravity). Uses the Math.sqrt() function.
   */
  public float getJumpVelocity(float jumpHeight) {
    // use velocity/acceleration formal: v*v = -2 * a(y-y0)
    // (v is jump velocity, a is accel, y-y0 is max height)
    return (float) Math.sqrt(-2 * gravityAccel * jumpHeight);
  }
}

/**
 * A Player object.
 */

class Player extends JumpingGameObject {

  public static final float DEFAULT_PLAYER_RADIUS = 32;

  public static final float DEFAULT_PLAYER_HEIGHT = 128;

  public Player() {
    this(new PolygonGroup("Player"));

    // set up player bounds
    PolygonGroupBounds playerBounds = getBounds();
    playerBounds.setTopHeight(DEFAULT_PLAYER_HEIGHT);
    playerBounds.setRadius(DEFAULT_PLAYER_RADIUS);
  }

  public Player(PolygonGroup group) {
    super(group);
  }

}

/**
 * The GridGameObjectManager is a GameObjectManager that integrally arranges
 * GameObjects on a 2D grid for visibility determination and to limit the number
 * of tests for collision detection.
 */

class GridGameObjectManager implements GameObjectManager {

  /**
   * Default grid size of 512. The grid size should be larger than the largest
   * object's diameter.
   */
  private static final int GRID_SIZE_BITS = 9;

  private static final int GRID_SIZE = 1 << GRID_SIZE_BITS;

  /**
   * The Cell class represents a cell in the grid. It contains a list of game
   * objects and a visible flag.
   */
  private static class Cell {
    List objects;

    boolean visible;

    Cell() {
      objects = new ArrayList();
      visible = false;
    }
  }

  private Cell[] grid;

  private Rectangle mapBounds;

  private int gridWidth;

  private int gridHeight;

  private List allObjects;

  private GameObject player;

  private Vector3D oldLocation;

  private CollisionDetection collisionDetection;

  /**
   * Creates a new GridGameObjectManager with the specified map bounds and
   * collision detection handler. GameObjects outside the map bounds will
   * never be shown.
   */
  public GridGameObjectManager(Rectangle mapBounds,
      CollisionDetection collisionDetection) {
    this.mapBounds = mapBounds;
    this.collisionDetection = collisionDetection;
    gridWidth = (mapBounds.width >> GRID_SIZE_BITS) + 1;
    gridHeight = (mapBounds.height >> GRID_SIZE_BITS) + 1;
    grid = new Cell[gridWidth * gridHeight];
    for (int i = 0; i < grid.length; i++) {
      grid[i] = new Cell();
    }
    allObjects = new ArrayList();
    oldLocation = new Vector3D();
  }

  /**
   * Converts a map x-coordinate to a grid x-coordinate.
   */
  private int convertMapXtoGridX(int x) {
    return (x - mapBounds.x) >> GRID_SIZE_BITS;
  }

  /**
   * Converts a map y-coordinate to a grid y-coordinate.
   */
  private int convertMapYtoGridY(int y) {
    return (y - mapBounds.y) >> GRID_SIZE_BITS;
  }

  /**
   * Marks all objects as potentially visible (should be drawn).
   */
  public void markAllVisible() {
    for (int i = 0; i < grid.length; i++) {
      grid[i].visible = true;
    }
  }

  /**
   * Marks all objects within the specified 2D bounds as potentially visible
   * (should be drawn).
   */
  public void markVisible(Rectangle bounds) {
    int x1 = Math.max(0, convertMapXtoGridX(bounds.x));
    int y1 = Math.max(0, convertMapYtoGridY(bounds.y));
    int x2 = Math.min(gridWidth - 1, convertMapXtoGridX(bounds.x
        + bounds.width));
    int y2 = Math.min(gridHeight - 1, convertMapYtoGridY(bounds.y
        + bounds.height));

    for (int y = y1; y <= y2; y++) {
      int offset = y * gridWidth;
      for (int x = x1; x <= x2; x++) {
        grid[offset + x].visible = true;
      }
    }
  }

  /**
   * Adds a GameObject to this manager.
   */
  public void add(GameObject object) {
    if (object != null) {
      if (object == player) {
        // ensure player always moves first
        allObjects.add(0, object);
      } else {
        allObjects.add(object);
      }
      Cell cell = getCell(object);
      if (cell != null) {
        cell.objects.add(object);
      }

    }
  }

  /**
   * Removes a GameObject from this manager.
   */
  public void remove(GameObject object) {
    if (object != null) {
      allObjects.remove(object);
      Cell cell = getCell(object);
      if (cell != null) {
        cell.objects.remove(object);
      }
    }
  }

  /**
   * Adds a GameObject to this manager, specifying it as the player object. An
   * existing player object, if any, is not removed.
   */
  public void addPlayer(GameObject player) {
    this.player = player;
    if (player != null) {
      player.notifyVisible(true);
      add(player);
    }
  }

  /**
   * Gets the object specified as the Player object, or null if no player
   * object was specified.
   */
  public GameObject getPlayer() {
    return player;
  }

  /**
   * Gets the cell the specified GameObject is in, or null if the GameObject
   * is not within the map bounds.
   */
  private Cell getCell(GameObject object) {
    int x = convertMapXtoGridX((int) object.getX());
    int y = convertMapYtoGridY((int) object.getZ());
    return getCell(x, y);
  }

  /**
   * Gets the cell of the specified grid location, or null if the grid
   * location is invalid.
   */
  private Cell getCell(int x, int y) {

    // check bounds
    if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight) {
      return null;
    }

    // get the cell at the x,y location
    return grid[x + y * gridWidth];
  }

  /**
   * Updates all objects based on the amount of time passed from the last
   * update and applied collision detection.
   */
  public void update(long elapsedTime) {
    for (int i = 0; i < allObjects.size(); i++) {
      GameObject object = (GameObject) allObjects.get(i);

      // save the object's old position
      Cell oldCell = getCell(object);
      oldLocation.setTo(object.getLocation());

      // move the object
      object.update(player, elapsedTime);

      // remove the object if destroyed
      if (object.isDestroyed()) {
        allObjects.remove(i);
        i--;
        if (oldCell != null) {
          oldCell.objects.remove(object);
        }
        continue;
      }

      // if the object moved, do collision detection
      if (!object.getLocation().equals(oldLocation)) {

        // check walls, floors, and ceilings
        collisionDetection.checkBSP(object, oldLocation, elapsedTime);

        // check other objects
        if (checkObjectCollision(object, oldLocation)) {
          // revert to old position
          object.getLocation().setTo(oldLocation);
        }

        // update grid location
        Cell cell = getCell(object);
        if (cell != oldCell) {
          if (oldCell != null) {
            oldCell.objects.remove(object);
          }
          if (cell != null) {
            cell.objects.add(object);
          }
        }
      }

    }
  }

  /**
   * Checks to see if the specified object collides with any other object.
   */
  public boolean checkObjectCollision(GameObject object, Vector3D oldLocation) {

    boolean collision = false;

    // use the object's (x,z) position (ground plane)
    int x = convertMapXtoGridX((int) object.getX());
    int y = convertMapYtoGridY((int) object.getZ());

    // check the object's surrounding 9 cells
    for (int i = x - 1; i <= x + 1; i++) {
      for (int j = y - 1; j <= y + 1; j++) {
        Cell cell = getCell(i, j);
        if (cell != null) {
          collision |= collisionDetection.checkObject(object,
              cell.objects, oldLocation);
        }
      }
    }

    return collision;
  }

  /**
   * Draws all visible objects and marks all objects as not visible.
   */
  public void draw(Graphics2D g, GameObjectRenderer r) {
    for (int i = 0; i < grid.length; i++) {
      List objects = grid[i].objects;
      for (int j = 0; j < objects.size(); j++) {
        GameObject object = (GameObject) objects.get(j);
        boolean visible = false;
        if (grid[i].visible) {
          visible = r.draw(g, object);
        }
        if (object != player) {
          // notify objects if they are visible
          object.notifyVisible(visible);
        }
      }
      grid[i].visible = false;
    }
  }
}

/**
 * The CollisionDetection class handles collision detection between the
 * GameObjects, and between GameObjects and a BSP tree. When a collision occurs,
 * the GameObject stops.
 */

class CollisionDetection {

  /**
   * Bounding game object corners used to test for intersection with the BSP
   * tree. Corners are in either clockwise or counter-clockwise order.
   */
  private static final Point2D.Float[] CORNERS = { new Point2D.Float(-1, -1),
      new Point2D.Float(-1, 1), new Point2D.Float(1, 1),
      new Point2D.Float(1, -1), };

  private BSPTree bspTree;

  private BSPLine path;

  private Point2D.Float intersection;

  /**
   * Creates a new CollisionDetection object for the specified BSP tree.
   */
  public CollisionDetection(BSPTree bspTree) {
    this.bspTree = bspTree;
    path = new BSPLine();
    intersection = new Point2D.Float();
  }

  /**
   * Checks a GameObject against the BSP tree. Returns true if a wall
   * collision occurred.
   */
  public boolean checkBSP(GameObject object, Vector3D oldLocation,
      long elapsedTime) {

    boolean wallCollision = false;

    // check walls if x or z position changed
    if (object.getX() != oldLocation.x || object.getZ() != oldLocation.z) {
      wallCollision = (checkWalls(object, oldLocation, elapsedTime) != null);
    }

    getFloorAndCeiling(object);
    checkFloorAndCeiling(object, elapsedTime);

    return wallCollision;
  }

  /**
   * Gets the floor and ceiling values for the specified GameObject. Calls
   * object.setFloorHeight() and object.setCeilHeight() to set the floor and
   * ceiling values.
   */
  public void getFloorAndCeiling(GameObject object) {
    float x = object.getX();
    float z = object.getZ();
    float r = object.getBounds().getRadius() - 1;
    float floorHeight = Float.MIN_VALUE;
    float ceilHeight = Float.MAX_VALUE;
    BSPTree.Leaf leaf = bspTree.getLeaf(x, z);
    if (leaf != null) {
      floorHeight = leaf.floorHeight;
      ceilHeight = leaf.ceilHeight;
    }

    // check surrounding four points
    for (int i = 0; i < CORNERS.length; i++) {
      float xOffset = r * CORNERS[i].x;
      float zOffset = r * CORNERS[i].y;
      leaf = bspTree.getLeaf(x + xOffset, z + zOffset);
      if (leaf != null) {
        floorHeight = Math.max(floorHeight, leaf.floorHeight);
        ceilHeight = Math.min(ceilHeight, leaf.ceilHeight);
      }
    }

    object.setFloorHeight(floorHeight);
    object.setCeilHeight(ceilHeight);
  }

  /**
   * Checks for object collisions with the floor and ceiling. Uses
   * object.getFloorHeight() and object.getCeilHeight() for the floor and
   * ceiling values.
   */
  protected void checkFloorAndCeiling(GameObject object, long elapsedTime) {
    boolean collision = false;

    float floorHeight = object.getFloorHeight();
    float ceilHeight = object.getCeilHeight();
    float bottomHeight = object.getBounds().getBottomHeight();
    float topHeight = object.getBounds().getTopHeight();

    if (!object.isFlying()) {
      object.getLocation().y = floorHeight - bottomHeight;
    }
    // check if below floor
    if (object.getY() + bottomHeight < floorHeight) {
      object.notifyFloorCollision();
      object.getTransform().getVelocity().y = 0;
      object.getLocation().y = floorHeight - bottomHeight;
    }
    // check if hitting ceiling
    else if (object.getY() + topHeight > ceilHeight) {
      object.notifyCeilingCollision();
      object.getTransform().getVelocity().y = 0;
      object.getLocation().y = ceilHeight - topHeight;
    }

  }

  /**
   * Checks for a game object collision with the walls of the BSP tree.
   * Returns the first wall collided with, or null if there was no collision.
   */
  public BSPPolygon checkWalls(GameObject object, Vector3D oldLocation,
      long elapsedTime) {
    Vector3D v = object.getTransform().getVelocity();
    PolygonGroupBounds bounds = object.getBounds();
    float x = object.getX();
    float y = object.getY();
    float z = object.getZ();
    float r = bounds.getRadius();
    float stepSize = 0;
    if (!object.isFlying()) {
      stepSize = BSPPolygon.PASSABLE_WALL_THRESHOLD;
    }
    float bottom = object.getY() + bounds.getBottomHeight() + stepSize;
    float top = object.getY() + bounds.getTopHeight();

    // pick closest intersection of 4 corners
    BSPPolygon closestWall = null;
    float closestDistSq = Float.MAX_VALUE;
    for (int i = 0; i < CORNERS.length; i++) {
      float xOffset = r * CORNERS[i].x;
      float zOffset = r * CORNERS[i].y;
      BSPPolygon wall = getFirstWallIntersection(oldLocation.x + xOffset,
          oldLocation.z + zOffset, x + xOffset, z + zOffset, bottom,
          top);
      if (wall != null) {
        float x2 = intersection.x - xOffset;
        float z2 = intersection.y - zOffset;
        float dx = (x2 - oldLocation.x);
        float dz = (z2 - oldLocation.z);
        float distSq = dx * dx + dz * dz;
        // pick the wall with the closest distance, or
        // if the distances are equal, pick the current
        // wall if the offset has the same sign as the
        // velocity.
        if (distSq < closestDistSq
            || (distSq == closestDistSq
                && MoreMath.sign(xOffset) == MoreMath.sign(v.x) && MoreMath
                .sign(zOffset) == MoreMath.sign(v.z))) {
          closestWall = wall;
          closestDistSq = distSq;
          object.getLocation().setTo(x2, y, z2);
        }
      }
    }

    if (closestWall != null) {
      object.notifyWallCollision();
    }

    // make sure the player bounds is empty
    // (avoid colliding with sharp corners)
    x = object.getX();
    z = object.getZ();
    r -= 1;
    for (int i = 0; i < CORNERS.length; i++) {
      int next = i + 1;
      if (next == CORNERS.length) {
        next = 0;
      }
      // use (r-1) so this doesn't interfere with normal
      // collisions
      float xOffset1 = r * CORNERS[i].x;
      float zOffset1 = r * CORNERS[i].y;
      float xOffset2 = r * CORNERS[next].x;
      float zOffset2 = r * CORNERS[next].y;

      BSPPolygon wall = getFirstWallIntersection(x + xOffset1, z
          + zOffset1, x + xOffset2, z + zOffset2, bottom, top);
      if (wall != null) {
        object.notifyWallCollision();
        object.getLocation().setTo(oldLocation.x, object.getY(),
            oldLocation.z);
        return wall;
      }
    }

    return closestWall;
  }

  /**
   * Gets the first intersection, if any, of the path (x1,z1)-> (x2,z2) with
   * the walls of the BSP tree. Returns the first BSPPolygon intersection, or
   * null if no intersection occurred.
   */
  public BSPPolygon getFirstWallIntersection(float x1, float z1, float x2,
      float z2, float yBottom, float yTop) {
    return getFirstWallIntersection(bspTree.getRoot(), x1, z1, x2, z2,
        yBottom, yTop);
  }

  /**
   * Gets the first intersection, if any, of the path (x1,z1)-> (x2,z2) with
   * the walls of the BSP tree, starting with the specified node. Returns the
   * first BSPPolyon intersection, or null if no intersection occurred.
   */
  protected BSPPolygon getFirstWallIntersection(BSPTree.Node node, float x1,
      float z1, float x2, float z2, float yBottom, float yTop) {
    if (node == null || node instanceof BSPTree.Leaf) {
      return null;
    }

    int start = node.partition.getSideThick(x1, z1);
    int end = node.partition.getSideThick(x2, z2);
    float intersectionX;
    float intersectionZ;

    if (end == BSPLine.COLLINEAR) {
      end = start;
    }

    if (start == BSPLine.COLLINEAR) {
      intersectionX = x1;
      intersectionZ = z1;
    } else if (start != end) {
      path.setLine(x1, z1, x2, z2);
      node.partition.getIntersectionPoint(path, intersection);
      intersectionX = intersection.x;
      intersectionZ = intersection.y;
    } else {
      intersectionX = x2;
      intersectionZ = z2;
    }

    if (start == BSPLine.COLLINEAR && start == end) {
      return null;
    }

    // check front part of line
    if (start != BSPLine.COLLINEAR) {
      BSPPolygon wall = getFirstWallIntersection(
          (start == BSPLine.FRONT) ? node.front : node.back, x1, z1,
          intersectionX, intersectionZ, yBottom, yTop);
      if (wall != null) {
        return wall;
      }
    }

    // test this boundary
    if (start != end || start == BSPLine.COLLINEAR) {
      BSPPolygon wall = getWallCollision(node.polygons, x1, z1, x2, z2,
          yBottom, yTop);
      if (wall != null) {
        intersection.setLocation(intersectionX, intersectionZ);
        return wall;
      }
    }

    // check back part of line
    if (start != end) {
      BSPPolygon wall = getFirstWallIntersection(
          (end == BSPLine.FRONT) ? node.front : node.back,
          intersectionX, intersectionZ, x2, z2, yBottom, yTop);
      if (wall != null) {
        return wall;
      }
    }

    // not found
    return null;
  }

  /**
   * Checks if the specified path collides with any of the collinear list of
   * polygons. The path crosses the line represented by the polygons, but the
   * polygons may not necessarily cross the path.
   */
  protected BSPPolygon getWallCollision(List polygons, float x1, float z1,
      float x2, float z2, float yBottom, float yTop) {
    path.setLine(x1, z1, x2, z2);
    for (int i = 0; i < polygons.size(); i++) {
      BSPPolygon poly = (BSPPolygon) polygons.get(i);
      BSPLine wall = poly.getLine();

      // check if not wall
      if (wall == null) {
        continue;
      }

      // check if not vertically in the wall (y axis)
      if (wall.top <= yBottom || wall.bottom > yTop) {
        continue;
      }

      // check if moving to back of wall
      if (wall.getSideThin(x2, z2) != BSPLine.BACK) {
        continue;
      }

      // check if path crosses wall
      int side1 = path.getSideThin(wall.x1, wall.y1);
      int side2 = path.getSideThin(wall.x2, wall.y2);
      if (side1 != side2) {
        return poly;
      }
    }
    return null;
  }

  /**
   * Checks if the specified object collisions with any other object in the
   * specified list.
   */
  public boolean checkObject(GameObject objectA, List objects,
      Vector3D oldLocation) {
    boolean collision = false;
    for (int i = 0; i < objects.size(); i++) {
      GameObject objectB = (GameObject) objects.get(i);
      collision |= checkObject(objectA, objectB, oldLocation);
    }
    return collision;
  }

  /**
   * Returns true if the two specified objects collide. Object A is the moving
   * object, and Object B is the object to check. Uses bounding upright
   * cylinders (circular base and top) to determine collisions.
   */
  public boolean checkObject(GameObject objectA, GameObject objectB,
      Vector3D oldLocation) {
    // don't collide with self
    if (objectA == objectB) {
      return false;
    }

    PolygonGroupBounds boundsA = objectA.getBounds();
    PolygonGroupBounds boundsB = objectB.getBounds();

    // first, check y axis collision (assume height is pos)
    float Ay1 = objectA.getY() + boundsA.getBottomHeight();
    float Ay2 = objectA.getY() + boundsA.getTopHeight();
    float By1 = objectB.getY() + boundsB.getBottomHeight();
    float By2 = objectB.getY() + boundsB.getTopHeight();
    if (By2 < Ay1 || By1 > Ay2) {
      return false;
    }

    // next, check 2D, x/z plane collision (circular base)
    float dx = objectA.getX() - objectB.getX();
    float dz = objectA.getZ() - objectB.getZ();
    float minDist = boundsA.getRadius() + boundsB.getRadius();
    float distSq = dx * dx + dz * dz;
    float minDistSq = minDist * minDist;
    if (distSq < minDistSq) {
      return handleObjectCollision(objectA, objectB, distSq, minDistSq,
          oldLocation);
    }
    return false;
  }

  /**
   * Handles an object collision. Object A is the moving object, and Object B
   * is the object that Object A collided with.
   */
  protected boolean handleObjectCollision(GameObject objectA,
      GameObject objectB, float distSq, float minDistSq,
      Vector3D oldLocation) {
    objectA.notifyObjectCollision(objectB);
    return true;
  }

}

/**
 * The PolygonGroupBounds represents a cylinder bounds around a PolygonGroup
 * that can be used for collision detection.
 */

class PolygonGroupBounds {

  private float topHeight;

  private float bottomHeight;

  private float radius;

  /**
   * Creates a new PolygonGroupBounds with no bounds.
   */
  public PolygonGroupBounds() {

  }

  /**
   * Creates a new PolygonGroupBounds with the bounds of the specified
   * PolygonGroup.
   */
  public PolygonGroupBounds(PolygonGroup group) {
    setToBounds(group);
  }

  /**
   * Sets this to the bounds of the specified PolygonGroup.
   */
  public void setToBounds(PolygonGroup group) {
    topHeight = Float.MIN_VALUE;
    bottomHeight = Float.MAX_VALUE;
    radius = 0;

    group.resetIterator();
    while (group.hasNext()) {
      Polygon3D poly = group.nextPolygon();
      for (int i = 0; i < poly.getNumVertices(); i++) {
        Vector3D v = poly.getVertex(i);
        topHeight = Math.max(topHeight, v.y);
        bottomHeight = Math.min(bottomHeight, v.y);
        // compute radius squared
        radius = Math.max(radius, v.x * v.x + v.z * v.z);
      }
    }

    if (radius == 0) {
      // empty polygon group!
      topHeight = 0;
      bottomHeight = 0;
    } else {
      radius = (float) Math.sqrt(radius);
    }
  }

  public float getTopHeight() {
    return topHeight;
  }

  public void setTopHeight(float topHeight) {
    this.topHeight = topHeight;
  }

  public float getBottomHeight() {
    return bottomHeight;
  }

  public void setBottomHeight(float bottomHeight) {
    this.bottomHeight = bottomHeight;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

}
/**
 * The PolygonGroup is a group of polygons with a MovingTransform3D.
 * PolygonGroups can also contain other PolygonGroups.
 */

class PolygonGroup implements Transformable {

  private String name;

  private String filename;

  private List objects;

  private MovingTransform3D transform;

  private int iteratorIndex;

  /**
   * Creates a new, empty PolygonGroup.
   */
  public PolygonGroup() {
    this("unnamed");
  }

  /**
   * Creates a new, empty PolygonGroup with te specified name.
   */
  public PolygonGroup(String name) {
    setName(name);
    objects = new ArrayList();
    transform = new MovingTransform3D();
    iteratorIndex = 0;
  }

  /**
   * Gets the MovingTransform3D for this PolygonGroup.
   */
  public MovingTransform3D getTransform() {
    return transform;
  }

  /**
   * Gets the name of this PolygonGroup.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this PolygonGroup.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the filename of this PolygonGroup.
   */
  public String getFilename() {
    return filename;
  }

  /**
   * Sets the filename of this PolygonGroup.
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * Adds a polygon to this group.
   */
  public void addPolygon(Polygon3D o) {
    objects.add(o);
  }

  /**
   * Adds a PolygonGroup to this group.
   */
  public void addPolygonGroup(PolygonGroup p) {
    objects.add(p);
  }

  /**
   * Clones this polygon group. Polygon3Ds are shared between this group and
   * the cloned group; Transform3Ds are copied.
   */
  public Object clone() {
    PolygonGroup group = new PolygonGroup(name);
    group.setFilename(filename);
    for (int i = 0; i < objects.size(); i++) {
      Object obj = objects.get(i);
      if (obj instanceof Polygon3D) {
        group.addPolygon((Polygon3D) obj);
      } else {
        PolygonGroup grp = (PolygonGroup) obj;
        group.addPolygonGroup((PolygonGroup) grp.clone());
      }
    }
    group.transform = (MovingTransform3D) transform.clone();
    return group;
  }

  /**
   * Gets the PolygonGroup in this group with the specified name, or null if
   * none found.
   */
  public PolygonGroup getGroup(String name) {
    // check for this group
    if (this.name != null && this.name.equals(name)) {
      return this;
    }
    for (int i = 0; i < objects.size(); i++) {
      Object obj = objects.get(i);
      if (obj instanceof PolygonGroup) {
        PolygonGroup subgroup = ((PolygonGroup) obj).getGroup(name);
        if (subgroup != null) {
          return subgroup;
        }
      }
    }

    // group not found
    return null;
  }

  /**
   * Resets the polygon iterator for this group.
   * 
   * @see #hasNext
   * @see #nextPolygon
   */
  public void resetIterator() {
    iteratorIndex = 0;
    for (int i = 0; i < objects.size(); i++) {
      Object obj = objects.get(i);
      if (obj instanceof PolygonGroup) {
        ((PolygonGroup) obj).resetIterator();
      }
    }
  }

  /**
   * Checks if there is another polygon in the current iteration.
   * 
   * @see #resetIterator
   * @see #nextPolygon
   */
  public boolean hasNext() {
    return (iteratorIndex < objects.size());
  }

  /**
   * Gets the next polygon in the current iteration.
   * 
   * @see #resetIterator
   * @see #hasNext
   */
  public Polygon3D nextPolygon() {
    Object obj = objects.get(iteratorIndex);

    if (obj instanceof PolygonGroup) {
      PolygonGroup group = (PolygonGroup) obj;
      Polygon3D poly = group.nextPolygon();
      if (!group.hasNext()) {
        iteratorIndex++;
      }
      return poly;
    } else {
      iteratorIndex++;
      return (Polygon3D) obj;
    }
  }

  /**
   * Gets the next polygon in the current iteration, applying the
   * MovingTransform3Ds to it, and storing it in 'cache'.
   */
  public void nextPolygonTransformed(Polygon3D cache) {
    Object obj = objects.get(iteratorIndex);

    if (obj instanceof PolygonGroup) {
      PolygonGroup group = (PolygonGroup) obj;
      group.nextPolygonTransformed(cache);
      if (!group.hasNext()) {
        iteratorIndex++;
      }
    } else {
      iteratorIndex++;
      cache.setTo((Polygon3D) obj);
    }

    cache.add(transform);
  }

  /**
   * Updates the MovingTransform3Ds of this group and any subgroups.
   */
  public void update(long elapsedTime) {
    transform.update(elapsedTime);
    for (int i = 0; i < objects.size(); i++) {
      Object obj = objects.get(i);
      if (obj instanceof PolygonGroup) {
        PolygonGroup group = (PolygonGroup) obj;
        group.update(elapsedTime);
      }
    }
  }

  // from the Transformable interface

  public void add(Vector3D u) {
    transform.getLocation().add(u);
  }

  public void subtract(Vector3D u) {
    transform.getLocation().subtract(u);
  }

  public void add(Transform3D xform) {
    addRotation(xform);
    add(xform.getLocation());
  }

  public void subtract(Transform3D xform) {
    subtract(xform.getLocation());
    subtractRotation(xform);
  }

  public void addRotation(Transform3D xform) {
    transform.rotateAngleX(xform.getAngleX());
    transform.rotateAngleY(xform.getAngleY());
    transform.rotateAngleZ(xform.getAngleZ());
  }

  public void subtractRotation(Transform3D xform) {
    transform.rotateAngleX(-xform.getAngleX());
    transform.rotateAngleY(-xform.getAngleY());
    transform.rotateAngleZ(-xform.getAngleZ());
  }

}

/**
 * The PolygonRenderer class is an abstract class that transforms and draws
 * polygons onto the screen.
 */

abstract class PolygonRenderer {

  protected ScanConverter scanConverter;

  protected Transform3D camera;

  protected ViewWindow viewWindow;

  protected boolean clearViewEveryFrame;

  protected Polygon3D sourcePolygon;

  protected Polygon3D destPolygon;

  /**
   * Creates a new PolygonRenderer with the specified Transform3D (camera) and
   * ViewWindow. The view is cleared when startFrame() is called.
   */
  public PolygonRenderer(Transform3D camera, ViewWindow viewWindow) {
    this(camera, viewWindow, true);
  }

  /**
   * Creates a new PolygonRenderer with the specified Transform3D (camera) and
   * ViewWindow. If clearViewEveryFrame is true, the view is cleared when
   * startFrame() is called.
   */
  public PolygonRenderer(Transform3D camera, ViewWindow viewWindow,
      boolean clearViewEveryFrame) {
    this.camera = camera;
    this.viewWindow = viewWindow;
    this.clearViewEveryFrame = clearViewEveryFrame;
    init();
  }

  /**
   * Create the scan converter and dest polygon.
   */
  protected void init() {
    destPolygon = new Polygon3D();
    scanConverter = new ScanConverter(viewWindow);
  }

  /**
   * Gets the camera used for this PolygonRenderer.
   */
  public Transform3D getCamera() {
    return camera;
  }

  /**
   * Indicates the start of rendering of a frame. This method should be called
   * every frame before any polygons are drawn.
   */
  public void startFrame(Graphics2D g) {
    if (clearViewEveryFrame) {
      g.setColor(Color.black);
      g.fillRect(viewWindow.getLeftOffset(), viewWindow.getTopOffset(),
          viewWindow.getWidth(), viewWindow.getHeight());
    }
  }

  /**
   * Indicates the end of rendering of a frame. This method should be called
   * every frame after all polygons are drawn.
   */
  public void endFrame(Graphics2D g) {
    // do nothing, for now.
  }

  /**
   * Transforms and draws a polygon.
   */
  public boolean draw(Graphics2D g, Polygon3D poly) {
    if (poly.isFacing(camera.getLocation())) {
      sourcePolygon = poly;
      destPolygon.setTo(poly);
      destPolygon.subtract(camera);
      boolean visible = destPolygon.clip(-1);
      if (visible) {
        destPolygon.project(viewWindow);
        visible = scanConverter.convert(destPolygon);
        if (visible) {
          drawCurrentPolygon(g);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Draws the current polygon. At this point, the current polygon is
   * transformed, clipped, projected, scan-converted, and visible.
   */
  protected abstract void drawCurrentPolygon(Graphics2D g);
}

/**
 * The ScanConverter class converts a projected polygon into a series of
 * horizontal scans for drawing.
 */

class ScanConverter {

  private static final int SCALE_BITS = 16;

  private static final int SCALE = 1 << SCALE_BITS;

  private static final int SCALE_MASK = SCALE - 1;

  protected ViewWindow view;

  protected Scan[] scans;

  protected int top;

  protected int bottom;

  /**
   * A horizontal scan line.
   */
  public static class Scan {
    public int left;

    public int right;

    /**
     * Sets the left and right boundary for this scan if the x value is
     * outside the current boundary.
     */
    public void setBoundary(int x) {
      if (x < left) {
        left = x;
      }
      if (x - 1 > right) {
        right = x - 1;
      }
    }

    /**
     * Clears this scan line.
     */
    public void clear() {
      left = Integer.MAX_VALUE;
      right = Integer.MIN_VALUE;
    }

    /**
     * Determines if this scan is valid (if left <= right).
     */
    public boolean isValid() {
      return (left <= right);
    }

    /**
     * Sets this scan.
     */
    public void setTo(int left, int right) {
      this.left = left;
      this.right = right;
    }

    /**
     * Checks if this scan is equal to the specified values.
     */
    public boolean equals(int left, int right) {
      return (this.left == left && this.right == right);
    }
  }

  /**
   * Creates a new ScanConverter for the specified ViewWindow. The
   * ViewWindow's properties can change in between scan conversions.
   */
  public ScanConverter(ViewWindow view) {
    this.view = view;
  }

  /**
   * Gets the top boundary of the last scan-converted polygon.
   */
  public int getTopBoundary() {
    return top;
  }

  /**
   * Gets the bottom boundary of the last scan-converted polygon.
   */
  public int getBottomBoundary() {
    return bottom;
  }

  /**
   * Gets the scan line for the specified y value.
   */
  public Scan getScan(int y) {
    return scans[y];
  }

  /**
   * Ensures this ScanConverter has the capacity to scan-convert a polygon to
   * the ViewWindow.
   */
  protected void ensureCapacity() {
    int height = view.getTopOffset() + view.getHeight();
    if (scans == null || scans.length != height) {
      scans = new Scan[height];
      for (int i = 0; i < height; i++) {
        scans[i] = new Scan();
      }
      // set top and bottom so clearCurrentScan clears all
      top = 0;
      bottom = height - 1;
    }

  }

  /**
   * Clears the current scan.
   */
  private void clearCurrentScan() {
    for (int i = top; i <= bottom; i++) {
      scans[i].clear();
    }
    top = Integer.MAX_VALUE;
    bottom = Integer.MIN_VALUE;
  }

  /**
   * Scan-converts a projected polygon. Returns true if the polygon is visible
   * in the view window.
   */
  public boolean convert(Polygon3D polygon) {

    ensureCapacity();
    clearCurrentScan();

    int minX = view.getLeftOffset();
    int maxX = view.getLeftOffset() + view.getWidth() - 1;
    int minY = view.getTopOffset();
    int maxY = view.getTopOffset() + view.getHeight() - 1;

    int numVertices = polygon.getNumVertices();
    for (int i = 0; i < numVertices; i++) {
      Vector3D v1 = polygon.getVertex(i);
      Vector3D v2;
      if (i == numVertices - 1) {
        v2 = polygon.getVertex(0);
      } else {
        v2 = polygon.getVertex(i + 1);
      }

      // ensure v1.y < v2.y
      if (v1.y > v2.y) {
        Vector3D temp = v1;
        v1 = v2;
        v2 = temp;
      }
      float dy = v2.y - v1.y;

      // ignore horizontal lines
      if (dy == 0) {
        continue;
      }

      int startY = Math.max(MoreMath.ceil(v1.y), minY);
      int endY = Math.min(MoreMath.ceil(v2.y) - 1, maxY);
      top = Math.min(top, startY);
      bottom = Math.max(bottom, endY);
      float dx = v2.x - v1.x;

      // special case: vertical line
      if (dx == 0) {
        int x = MoreMath.ceil(v1.x);
        // ensure x within view bounds
        x = Math.min(maxX + 1, Math.max(x, minX));
        for (int y = startY; y <= endY; y++) {
          scans[y].setBoundary(x);
        }
      } else {
        // scan-convert this edge (line equation)
        float gradient = dx / dy;

        // (slower version)
        /*
         * for (int y=startY; y <=endY; y++) { int x =
         * MoreMath.ceil(v1.x + (y - v1.y) * gradient); // ensure x
         * within view bounds x = Math.min(maxX+1, Math.max(x, minX));
         * scans[y].setBoundary(x); }
         */

        // (faster version)
        // trim start of line
        float startX = v1.x + (startY - v1.y) * gradient;
        if (startX < minX) {
          int yInt = (int) (v1.y + (minX - v1.x) / gradient);
          yInt = Math.min(yInt, endY);
          while (startY <= yInt) {
            scans[startY].setBoundary(minX);
            startY++;
          }
        } else if (startX > maxX) {
          int yInt = (int) (v1.y + (maxX - v1.x) / gradient);
          yInt = Math.min(yInt, endY);
          while (startY <= yInt) {
            scans[startY].setBoundary(maxX + 1);
            startY++;
          }
        }

        if (startY > endY) {
          continue;
        }

        // trim back of line
        float endX = v1.x + (endY - v1.y) * gradient;
        if (endX < minX) {
          int yInt = MoreMath.ceil(v1.y + (minX - v1.x) / gradient);
          yInt = Math.max(yInt, startY);
          while (endY >= yInt) {
            scans[endY].setBoundary(minX);
            endY--;
          }
        } else if (endX > maxX) {
          int yInt = MoreMath.ceil(v1.y + (maxX - v1.x) / gradient);
          yInt = Math.max(yInt, startY);
          while (endY >= yInt) {
            scans[endY].setBoundary(maxX + 1);
            endY--;
          }
        }

        if (startY > endY) {
          continue;
        }

        // line equation using integers
        int xScaled = (int) (SCALE * v1.x + SCALE * (startY - v1.y)
            * dx / dy)
            + SCALE_MASK;
        int dxScaled = (int) (dx * SCALE / dy);

        for (int y = startY; y <= endY; y++) {
          scans[y].setBoundary(xScaled >> SCALE_BITS);
          xScaled += dxScaled;
        }
      }
    }

    // check if visible (any valid scans)
    for (int i = top; i <= bottom; i++) {
      if (scans[i].isValid()) {
        return true;
      }
    }
    return false;
  }

}

/**
 * The MoreMath class provides functions not contained in the java.lang.Math or
 * java.lang.StrictMath classes.
 */

class MoreMath {

  /**
   * Returns the sign of the number. Returns -1 for negative, 1 for positive,
   * and 0 otherwise.
   */
  public static int sign(short v) {
    return (v > 0) ? 1 : (v < 0) ? -1 : 0;
  }

  /**
   * Returns the sign of the number. Returns -1 for negative, 1 for positive,
   * and 0 otherwise.
   */
  public static int sign(int v) {
    return (v > 0) ? 1 : (v < 0) ? -1 : 0;
  }

  /**
   * Returns the sign of the number. Returns -1 for negative, 1 for positive,
   * and 0 otherwise.
   */
  public static int sign(long v) {
    return (v > 0) ? 1 : (v < 0) ? -1 : 0;
  }

  /**
   * Returns the sign of the number. Returns -1 for negative, 1 for positive,
   * and 0 otherwise.
   */
  public static int sign(float v) {
    return (v > 0) ? 1 : (v < 0) ? -1 : 0;
  }

  /**
   * Returns the sign of the number. Returns -1 for negative, 1 for positive,
   * and 0 otherwise.
   */
  public static int sign(double v) {
    return (v > 0) ? 1 : (v < 0) ? -1 : 0;
  }

  /**
   * Faster ceil function to convert a float to an int. Contrary to the
   * java.lang.Math ceil function, this function takes a float as an argument,
   * returns an int instead of a double, and does not consider special cases.
   */
  public static int ceil(float f) {
    if (f > 0) {
      return (int) f + 1;
    } else {
      return (int) f;
    }
  }

  /**
   * Faster floor function to convert a float to an int. Contrary to the
   * java.lang.Math floor function, this function takes a float as an
   * argument, returns an int instead of a double, and does not consider
   * special cases.
   */
  public static int floor(float f) {
    if (f >= 0) {
      return (int) f;
    } else {
      return (int) f - 1;
    }
  }

  /**
   * Returns true if the specified number is a power of 2.
   */
  public static boolean isPowerOfTwo(int n) {
    return ((n & (n - 1)) == 0);
  }

  /**
   * Gets the number of "on" bits in an integer.
   */
  public static int getBitCount(int n) {
    int count = 0;
    while (n > 0) {
      count += (n & 1);
      n >>= 1;
    }
    return count;
  }
}

/**
 * The ViewWindow class represents the geometry of a view window for 3D viewing.
 */

class ViewWindow {

  private Rectangle bounds;

  private float angle;

  private float distanceToCamera;

  /**
   * Creates a new ViewWindow with the specified bounds on the screen and
   * horizontal view angle.
   */
  public ViewWindow(int left, int top, int width, int height, float angle) {
    bounds = new Rectangle();
    this.angle = angle;
    setBounds(left, top, width, height);
  }

  /**
   * Sets the bounds for this ViewWindow on the screen.
   */
  public void setBounds(int left, int top, int width, int height) {
    bounds.x = left;
    bounds.y = top;
    bounds.width = width;
    bounds.height = height;
    distanceToCamera = (bounds.width / 2) / (float) Math.tan(angle / 2);
  }

  /**
   * Sets the horizontal view angle for this ViewWindow.
   */
  public void setAngle(float angle) {
    this.angle = angle;
    distanceToCamera = (bounds.width / 2) / (float) Math.tan(angle / 2);
  }

  /**
   * Gets the horizontal view angle of this view window.
   */
  public float getAngle() {
    return angle;
  }

  /**
   * Gets the width of this view window.
   */
  public int getWidth() {
    return bounds.width;
  }

  /**
   * Gets the height of this view window.
   */
  public int getHeight() {
    return bounds.height;
  }

  /**
   * Gets the y offset of this view window on the screen.
   */
  public int getTopOffset() {
    return bounds.y;
  }

  /**
   * Gets the x offset of this view window on the screen.
   */
  public int getLeftOffset() {
    return bounds.x;
  }

  /**
   * Gets the distance from the camera to to this view window.
   */
  public float getDistance() {
    return distanceToCamera;
  }

  /**
   * Converts an x coordinate on this view window to the corresponding x
   * coordinate on the screen.
   */
  public float convertFromViewXToScreenX(float x) {
    return x + bounds.x + bounds.width / 2;
  }

  /**
   * Converts a y coordinate on this view window to the corresponding y
   * coordinate on the screen.
   */
  public float convertFromViewYToScreenY(float y) {
    return -y + bounds.y + bounds.height / 2;
  }

  /**
   * Converts an x coordinate on the screen to the corresponding x coordinate
   * on this view window.
   */
  public float convertFromScreenXToViewX(float x) {
    return x - bounds.x - bounds.width / 2;
  }

  /**
   * Converts an y coordinate on the screen to the corresponding y coordinate
   * on this view window.
   */
  public float convertFromScreenYToViewY(float y) {
    return -y + bounds.y + bounds.height / 2;
  }

  /**
   * Projects the specified vector to the screen.
   */
  public void project(Vector3D v) {
    // project to view window
    v.x = distanceToCamera * v.x / -v.z;
    v.y = distanceToCamera * v.y / -v.z;

    // convert to screen coordinates
    v.x = convertFromViewXToScreenX(v.x);
    v.y = convertFromViewYToScreenY(v.y);
  }
}

/**
 * A Rectangle3D is a rectangle in 3D space, defined as an origin and vectors
 * pointing in the directions of the base (width) and side (height).
 */

class Rectangle3D implements Transformable {

  private Vector3D origin;

  private Vector3D directionU;

  private Vector3D directionV;

  private Vector3D normal;

  private float width;

  private float height;

  /**
   * Creates a rectangle at the origin with a width and height of zero.
   */
  public Rectangle3D() {
    origin = new Vector3D();
    directionU = new Vector3D(1, 0, 0);
    directionV = new Vector3D(0, 1, 0);
    width = 0;
    height = 0;
  }

  /**
   * Creates a new Rectangle3D with the specified origin, direction of the
   * base (directionU) and direction of the side (directionV).
   */
  public Rectangle3D(Vector3D origin, Vector3D directionU,
      Vector3D directionV, float width, float height) {
    this.origin = new Vector3D(origin);
    this.directionU = new Vector3D(directionU);
    this.directionU.normalize();
    this.directionV = new Vector3D(directionV);
    this.directionV.normalize();
    this.width = width;
    this.height = height;
  }

  /**
   * Sets the values of this Rectangle3D to the specified Rectangle3D.
   */
  public void setTo(Rectangle3D rect) {
    origin.setTo(rect.origin);
    directionU.setTo(rect.directionU);
    directionV.setTo(rect.directionV);
    width = rect.width;
    height = rect.height;
  }

  /**
   * Gets the origin of this Rectangle3D.
   */
  public Vector3D getOrigin() {
    return origin;
  }

  /**
   * Gets the direction of the base of this Rectangle3D.
   */
  public Vector3D getDirectionU() {
    return directionU;
  }

  /**
   * Gets the direction of the side of this Rectangle3D.
   */
  public Vector3D getDirectionV() {
    return directionV;
  }

  /**
   * Gets the width of this Rectangle3D.
   */
  public float getWidth() {
    return width;
  }

  /**
   * Sets the width of this Rectangle3D.
   */
  public void setWidth(float width) {
    this.width = width;
  }

  /**
   * Gets the height of this Rectangle3D.
   */
  public float getHeight() {
    return height;
  }

  /**
   * Sets the height of this Rectangle3D.
   */
  public void setHeight(float height) {
    this.height = height;
  }

  /**
   * Calculates the normal vector of this Rectange3D.
   */
  protected Vector3D calcNormal() {
    if (normal == null) {
      normal = new Vector3D();
    }
    normal.setToCrossProduct(directionU, directionV);
    normal.normalize();
    return normal;
  }

  /**
   * Gets the normal of this Rectangle3D.
   */
  public Vector3D getNormal() {
    if (normal == null) {
      calcNormal();
    }
    return normal;
  }

  /**
   * Sets the normal of this Rectangle3D.
   */
  public void setNormal(Vector3D n) {
    if (normal == null) {
      normal = new Vector3D(n);
    } else {
      normal.setTo(n);
    }
  }

  public void add(Vector3D u) {
    origin.add(u);
    // don't translate direction vectors or size
  }

  public void subtract(Vector3D u) {
    origin.subtract(u);
    // don't translate direction vectors or size
  }

  public void add(Transform3D xform) {
    addRotation(xform);
    add(xform.getLocation());
  }

  public void subtract(Transform3D xform) {
    subtract(xform.getLocation());
    subtractRotation(xform);
  }

  public void addRotation(Transform3D xform) {
    origin.addRotation(xform);
    directionU.addRotation(xform);
    directionV.addRotation(xform);
  }

  public void subtractRotation(Transform3D xform) {
    origin.subtractRotation(xform);
    directionU.subtractRotation(xform);
    directionV.subtractRotation(xform);
  }

}

/**
 * The Polygon3D class represents a polygon as a series of vertices.
 */

class Polygon3D implements Transformable {

  // temporary vectors used for calculation
  private static Vector3D temp1 = new Vector3D();

  private static Vector3D temp2 = new Vector3D();

  private Vector3D[] v;

  private int numVertices;

  private Vector3D normal;

  /**
   * Creates an empty polygon that can be used as a "scratch" polygon for
   * transforms, projections, etc.
   */
  public Polygon3D() {
    numVertices = 0;
    v = new Vector3D[0];
    normal = new Vector3D();
  }

  /**
   * Creates a new Polygon3D with the specified vertices.
   */
  public Polygon3D(Vector3D v0, Vector3D v1, Vector3D v2) {
    this(new Vector3D[] { v0, v1, v2 });
  }

  /**
   * Creates a new Polygon3D with the specified vertices. All the vertices are
   * assumed to be in the same plane.
   */
  public Polygon3D(Vector3D v0, Vector3D v1, Vector3D v2, Vector3D v3) {
    this(new Vector3D[] { v0, v1, v2, v3 });
  }

  /**
   * Creates a new Polygon3D with the specified vertices. All the vertices are
   * assumed to be in the same plane.
   */
  public Polygon3D(Vector3D[] vertices) {
    this.v = vertices;
    numVertices = vertices.length;
    calcNormal();
  }

  /**
   * Sets this polygon to the same vertices as the specfied polygon.
   */
  public void setTo(Polygon3D polygon) {
    numVertices = polygon.numVertices;
    normal.setTo(polygon.normal);

    ensureCapacity(numVertices);
    for (int i = 0; i < numVertices; i++) {
      v[i].setTo(polygon.v[i]);
    }
  }

  /**
   * Ensures this polgon has enough capacity to hold the specified number of
   * vertices.
   */
  protected void ensureCapacity(int length) {
    if (v.length < length) {
      Vector3D[] newV = new Vector3D[length];
      System.arraycopy(v, 0, newV, 0, v.length);
      for (int i = v.length; i < newV.length; i++) {
        newV[i] = new Vector3D();
      }
      v = newV;
    }
  }

  /**
   * Gets the number of vertices this polygon has.
   */
  public int getNumVertices() {
    return numVertices;
  }

  /**
   * Gets the vertex at the specified index.
   */
  public Vector3D getVertex(int index) {
    return v[index];
  }

  /**
   * Projects this polygon onto the view window.
   */
  public void project(ViewWindow view) {
    for (int i = 0; i < numVertices; i++) {
      view.project(v[i]);
    }
  }

  // methods from the Transformable interface.

  public void add(Vector3D u) {
    for (int i = 0; i < numVertices; i++) {
      v[i].add(u);
    }
  }

  public void subtract(Vector3D u) {
    for (int i = 0; i < numVertices; i++) {
      v[i].subtract(u);
    }
  }

  public void add(Transform3D xform) {
    addRotation(xform);
    add(xform.getLocation());
  }

  public void subtract(Transform3D xform) {
    subtract(xform.getLocation());
    subtractRotation(xform);
  }

  public void addRotation(Transform3D xform) {
    for (int i = 0; i < numVertices; i++) {
      v[i].addRotation(xform);
    }
    normal.addRotation(xform);
  }

  public void subtractRotation(Transform3D xform) {
    for (int i = 0; i < numVertices; i++) {
      v[i].subtractRotation(xform);
    }
    normal.subtractRotation(xform);
  }

  /**
   * Calculates the unit-vector normal of this polygon. This method uses the
   * first, second, and third vertices to calcuate the normal, so if these
   * vertices are collinear, this method will not work. In this case, you can
   * get the normal from the bounding rectangle. Use setNormal() to explicitly
   * set the normal. This method uses static objects in the Polygon3D class
   * for calculations, so this method is not thread-safe across all instances
   * of Polygon3D.
   */
  public Vector3D calcNormal() {
    if (normal == null) {
      normal = new Vector3D();
    }
    temp1.setTo(v[2]);
    temp1.subtract(v[1]);
    temp2.setTo(v[0]);
    temp2.subtract(v[1]);
    normal.setToCrossProduct(temp1, temp2);
    normal.normalize();
    return normal;
  }

  /**
   * Gets the normal of this polygon. Use calcNormal() if any vertices have
   * changed.
   */
  public Vector3D getNormal() {
    return normal;
  }

  /**
   * Sets the normal of this polygon.
   */
  public void setNormal(Vector3D n) {
    if (normal == null) {
      normal = new Vector3D(n);
    } else {
      normal.setTo(n);
    }
  }

  /**
   * Tests if this polygon is facing the specified location. This method uses
   * static objects in the Polygon3D class for calculations, so this method is
   * not thread-safe across all instances of Polygon3D.
   */
  public boolean isFacing(Vector3D u) {
    temp1.setTo(u);
    temp1.subtract(v[0]);
    return (normal.getDotProduct(temp1) >= 0);
  }

  /**
   * Clips this polygon so that all vertices are in front of the clip plane,
   * clipZ (in other words, all vertices have z <= clipZ). The value of clipZ
   * should not be 0, as this causes divide-by-zero problems. Returns true if
   * the polygon is at least partially in front of the clip plane.
   */
  public boolean clip(float clipZ) {
    ensureCapacity(numVertices * 3);

    boolean isCompletelyHidden = true;

    // insert vertices so all edges are either completly
    // in front or behind the clip plane
    for (int i = 0; i < numVertices; i++) {
      int next = (i + 1) % numVertices;
      Vector3D v1 = v[i];
      Vector3D v2 = v[next];
      if (v1.z < clipZ) {
        isCompletelyHidden = false;
      }
      // ensure v1.z < v2.z
      if (v1.z > v2.z) {
        Vector3D temp = v1;
        v1 = v2;
        v2 = temp;
      }
      if (v1.z < clipZ && v2.z > clipZ) {
        float scale = (clipZ - v1.z) / (v2.z - v1.z);
        insertVertex(next, v1.x + scale * (v2.x - v1.x), v1.y + scale
            * (v2.y - v1.y), clipZ);
        // skip the vertex we just created
        i++;
      }
    }

    if (isCompletelyHidden) {
      return false;
    }

    // delete all vertices that have z > clipZ
    for (int i = numVertices - 1; i >= 0; i--) {
      if (v[i].z > clipZ) {
        deleteVertex(i);
      }
    }

    return (numVertices >= 3);
  }

  /**
   * Inserts a new vertex at the specified index.
   */
  protected void insertVertex(int index, float x, float y, float z) {
    Vector3D newVertex = v[v.length - 1];
    newVertex.x = x;
    newVertex.y = y;
    newVertex.z = z;
    for (int i = v.length - 1; i > index; i--) {
      v[i] = v[i - 1];
    }
    v[index] = newVertex;
    numVertices++;
  }

  /**
   * Delete the vertex at the specified index.
   */
  protected void deleteVertex(int index) {
    Vector3D deleted = v[index];
    for (int i = index; i < v.length - 1; i++) {
      v[i] = v[i + 1];
    }
    v[v.length - 1] = deleted;
    numVertices--;
  }

  /**
   * Inserts a vertex into this polygon at the specified index. The exact
   * vertex in inserted (not a copy).
   */
  public void insertVertex(int index, Vector3D vertex) {
    Vector3D[] newV = new Vector3D[numVertices + 1];
    System.arraycopy(v, 0, newV, 0, index);
    newV[index] = vertex;
    System.arraycopy(v, index, newV, index + 1, numVertices - index);
    v = newV;
    numVertices++;
  }

  /**
   * Calculates and returns the smallest bounding rectangle for this polygon.
   */
  public Rectangle3D calcBoundingRectangle() {

    // the smallest bounding rectangle for a polygon shares
    // at least one edge with the polygon. so, this method
    // finds the bounding rectangle for every edge in the
    // polygon, and returns the smallest one.
    Rectangle3D boundingRect = new Rectangle3D();
    float minimumArea = Float.MAX_VALUE;
    Vector3D u = new Vector3D();
    Vector3D v = new Vector3D();
    Vector3D d = new Vector3D();
    for (int i = 0; i < getNumVertices(); i++) {
      u.setTo(getVertex((i + 1) % getNumVertices()));
      u.subtract(getVertex(i));
      u.normalize();
      v.setToCrossProduct(getNormal(), u);
      v.normalize();

      float uMin = 0;
      float uMax = 0;
      float vMin = 0;
      float vMax = 0;
      for (int j = 0; j < getNumVertices(); j++) {
        if (j != i) {
          d.setTo(getVertex(j));
          d.subtract(getVertex(i));
          float uLength = d.getDotProduct(u);
          float vLength = d.getDotProduct(v);
          uMin = Math.min(uLength, uMin);
          uMax = Math.max(uLength, uMax);
          vMin = Math.min(vLength, vMin);
          vMax = Math.max(vLength, vMax);
        }
      }
      // if this calculated area is the smallest, set
      // the bounding rectangle
      float area = (uMax - uMin) * (vMax - vMin);
      if (area < minimumArea) {
        minimumArea = area;
        Vector3D origin = boundingRect.getOrigin();
        origin.setTo(getVertex(i));
        d.setTo(u);
        d.multiply(uMin);
        origin.add(d);
        d.setTo(v);
        d.multiply(vMin);
        origin.add(d);
        boundingRect.getDirectionU().setTo(u);
        boundingRect.getDirectionV().setTo(v);
        boundingRect.setWidth(uMax - uMin);
        boundingRect.setHeight(vMax - vMin);
      }
    }
    return boundingRect;
  }
}

/**
 * The Transform3D class represents a rotation and translation.
 */

class Transform3D {

  protected Vector3D location;

  private float cosAngleX;

  private float sinAngleX;

  private float cosAngleY;

  private float sinAngleY;

  private float cosAngleZ;

  private float sinAngleZ;

  /**
   * Creates a new Transform3D with no translation or rotation.
   */
  public Transform3D() {
    this(0, 0, 0);
  }

  /**
   * Creates a new Transform3D with the specified translation and no rotation.
   */
  public Transform3D(float x, float y, float z) {
    location = new Vector3D(x, y, z);
    setAngle(0, 0, 0);
  }

  /**
   * Creates a new Transform3D
   */
  public Transform3D(Transform3D v) {
    location = new Vector3D();
    setTo(v);
  }

  public Object clone() {
    return new Transform3D(this);
  }

  /**
   * Sets this Transform3D to the specified Transform3D.
   */
  public void setTo(Transform3D v) {
    location.setTo(v.location);
    this.cosAngleX = v.cosAngleX;
    this.sinAngleX = v.sinAngleX;
    this.cosAngleY = v.cosAngleY;
    this.sinAngleY = v.sinAngleY;
    this.cosAngleZ = v.cosAngleZ;
    this.sinAngleZ = v.sinAngleZ;
  }

  /**
   * Gets the location (translation) of this transform.
   */
  public Vector3D getLocation() {
    return location;
  }

  public float getCosAngleX() {
    return cosAngleX;
  }

  public float getSinAngleX() {
    return sinAngleX;
  }

  public float getCosAngleY() {
    return cosAngleY;
  }

  public float getSinAngleY() {
    return sinAngleY;
  }

  public float getCosAngleZ() {
    return cosAngleZ;
  }

  public float getSinAngleZ() {
    return sinAngleZ;
  }

  public float getAngleX() {
    return (float) Math.atan2(sinAngleX, cosAngleX);
  }

  public float getAngleY() {
    return (float) Math.atan2(sinAngleY, cosAngleY);
  }

  public float getAngleZ() {
    return (float) Math.atan2(sinAngleZ, cosAngleZ);
  }

  public void setAngleX(float angleX) {
    cosAngleX = (float) Math.cos(angleX);
    sinAngleX = (float) Math.sin(angleX);
  }

  public void setAngleY(float angleY) {
    cosAngleY = (float) Math.cos(angleY);
    sinAngleY = (float) Math.sin(angleY);
  }

  public void setAngleZ(float angleZ) {
    cosAngleZ = (float) Math.cos(angleZ);
    sinAngleZ = (float) Math.sin(angleZ);
  }

  public void setAngle(float angleX, float angleY, float angleZ) {
    setAngleX(angleX);
    setAngleY(angleY);
    setAngleZ(angleZ);
  }

  public void rotateAngleX(float angle) {
    if (angle != 0) {
      setAngleX(getAngleX() + angle);
    }
  }

  public void rotateAngleY(float angle) {
    if (angle != 0) {
      setAngleY(getAngleY() + angle);
    }
  }

  public void rotateAngleZ(float angle) {
    if (angle != 0) {
      setAngleZ(getAngleZ() + angle);
    }
  }

  public void rotateAngle(float angleX, float angleY, float angleZ) {
    rotateAngleX(angleX);
    rotateAngleY(angleY);
    rotateAngleZ(angleZ);
  }

}

interface Transformable {

  public void add(Vector3D u);

  public void subtract(Vector3D u);

  public void add(Transform3D xform);

  public void subtract(Transform3D xform);

  public void addRotation(Transform3D xform);

  public void subtractRotation(Transform3D xform);

}

/**
 * A MovingTransform3D is a Transform3D that has a location velocity and a
 * angular rotation velocity for rotation around the x, y, and z axes.
 */

class MovingTransform3D extends Transform3D {

  public static final int FOREVER = -1;

  // Vector3D used for calculations
  private static Vector3D temp = new Vector3D();

  // velocity (units per millisecond)
  private Vector3D velocity;

  private Movement velocityMovement;

  // angular velocity (radians per millisecond)
  private Movement velocityAngleX;

  private Movement velocityAngleY;

  private Movement velocityAngleZ;

  /**
   * Creates a new MovingTransform3D
   */
  public MovingTransform3D() {
    init();
  }

  /**
   * Creates a new MovingTransform3D, using the same values as the specified
   * Transform3D.
   */
  public MovingTransform3D(Transform3D v) {
    super(v);
    init();
  }

  protected void init() {
    velocity = new Vector3D(0, 0, 0);
    velocityMovement = new Movement();
    velocityAngleX = new Movement();
    velocityAngleY = new Movement();
    velocityAngleZ = new Movement();
  }

  public Object clone() {
    return new MovingTransform3D(this);
  }

  /**
   * Updates this Transform3D based on the specified elapsed time. The
   * location and angles are updated.
   */
  public void update(long elapsedTime) {
    float delta = velocityMovement.getDistance(elapsedTime);
    if (delta != 0) {
      temp.setTo(velocity);
      temp.multiply(delta);
      location.add(temp);
    }

    rotateAngle(velocityAngleX.getDistance(elapsedTime), velocityAngleY
        .getDistance(elapsedTime), velocityAngleZ
        .getDistance(elapsedTime));
  }

  /**
   * Stops this Transform3D. Any moving velocities are set to zero.
   */
  public void stop() {
    velocity.setTo(0, 0, 0);
    velocityMovement.set(0, 0);
    velocityAngleX.set(0, 0);
    velocityAngleY.set(0, 0);
    velocityAngleZ.set(0, 0);
  }

  /**
   * Sets the velocity to move to the following destination at the specified
   * speed.
   */
  public void moveTo(Vector3D destination, float speed) {
    temp.setTo(destination);
    temp.subtract(location);

    // calc the time needed to move
    float distance = temp.length();
    long time = (long) (distance / speed);

    // normalize the direction vector
    temp.divide(distance);
    temp.multiply(speed);

    setVelocity(temp, time);
  }

  /**
   * Returns true if currently moving.
   */
  public boolean isMoving() {
    return !velocityMovement.isStopped() && !velocity.equals(0, 0, 0);
  }

  /**
   * Returns true if currently moving, ignoring the y movement.
   */
  public boolean isMovingIgnoreY() {
    return !velocityMovement.isStopped()
        && (velocity.x != 0 || velocity.z != 0);
  }

  /**
   * Gets the amount of time remaining for this movement.
   */
  public long getRemainingMoveTime() {
    if (!isMoving()) {
      return 0;
    } else {
      return velocityMovement.remainingTime;
    }
  }

  /**
   * Gets the velocity vector. If the velocity vector is modified directly,
   * call setVelocity() to ensure the change is recognized.
   */
  public Vector3D getVelocity() {
    return velocity;
  }

  /**
   * Sets the velocity to the specified vector.
   */
  public void setVelocity(Vector3D v) {
    setVelocity(v, FOREVER);
  }

  /**
   * Sets the velocity. The velocity is automatically set to zero after the
   * specified amount of time has elapsed. If the specified time is FOREVER,
   * then the velocity is never automatically set to zero.
   */
  public void setVelocity(Vector3D v, long time) {
    if (velocity != v) {
      velocity.setTo(v);
    }
    if (v.x == 0 && v.y == 0 && v.z == 0) {
      velocityMovement.set(0, 0);
    } else {
      velocityMovement.set(1, time);
    }

  }

  /**
   * Adds the specified velocity to the current velocity. If this
   * MovingTransform3D is currently moving, it's time remaining is not
   * changed. Otherwise, the time remaining is set to FOREVER.
   */
  public void addVelocity(Vector3D v) {
    if (isMoving()) {
      velocity.add(v);
    } else {
      setVelocity(v);
    }
  }

  /**
   * Turns the x axis to the specified angle with the specified speed.
   */
  public void turnXTo(float angleDest, float speed) {
    turnTo(velocityAngleX, getAngleX(), angleDest, speed);
  }

  /**
   * Turns the y axis to the specified angle with the specified speed.
   */
  public void turnYTo(float angleDest, float speed) {
    turnTo(velocityAngleY, getAngleY(), angleDest, speed);
  }

  /**
   * Turns the z axis to the specified angle with the specified speed.
   */
  public void turnZTo(float angleDest, float speed) {
    turnTo(velocityAngleZ, getAngleZ(), angleDest, speed);
  }

  /**
   * Turns the x axis to face the specified (y,z) vector direction with the
   * specified speed.
   */
  public void turnXTo(float y, float z, float angleOffset, float speed) {
    turnXTo((float) Math.atan2(-z, y) + angleOffset, speed);
  }

  /**
   * Turns the y axis to face the specified (x,z) vector direction with the
   * specified speed.
   */
  public void turnYTo(float x, float z, float angleOffset, float speed) {
    turnYTo((float) Math.atan2(-z, x) + angleOffset, speed);
  }

  /**
   * Turns the z axis to face the specified (x,y) vector direction with the
   * specified speed.
   */
  public void turnZTo(float x, float y, float angleOffset, float speed) {
    turnZTo((float) Math.atan2(y, x) + angleOffset, speed);
  }

  /**
   * Ensures the specified angle is with -pi and pi. Returns the angle,
   * corrected if it is not within these bounds.
   */
  protected float ensureAngleWithinBounds(float angle) {
    if (angle < -Math.PI || angle > Math.PI) {
      // transform range to (0 to 1)
      double newAngle = (angle + Math.PI) / (2 * Math.PI);
      // validate range
      newAngle = newAngle - Math.floor(newAngle);
      // transform back to (-pi to pi) range
      newAngle = Math.PI * (newAngle * 2 - 1);
      return (float) newAngle;
    }
    return angle;
  }

  /**
   * Turns the movement angle from the startAngle to the endAngle with the
   * specified speed.
   */
  protected void turnTo(Movement movement, float startAngle, float endAngle,
      float speed) {
    startAngle = ensureAngleWithinBounds(startAngle);
    endAngle = ensureAngleWithinBounds(endAngle);
    if (startAngle == endAngle) {
      movement.set(0, 0);
    } else {

      float distanceLeft;
      float distanceRight;
      float pi2 = (float) (2 * Math.PI);

      if (startAngle < endAngle) {
        distanceLeft = startAngle - endAngle + pi2;
        distanceRight = endAngle - startAngle;
      } else {
        distanceLeft = startAngle - endAngle;
        distanceRight = endAngle - startAngle + pi2;
      }

      if (distanceLeft < distanceRight) {
        speed = -Math.abs(speed);
        movement.set(speed, (long) (distanceLeft / -speed));
      } else {
        speed = Math.abs(speed);
        movement.set(speed, (long) (distanceRight / speed));
      }
    }
  }

  /**
   * Sets the angular speed of the x axis.
   */
  public void setAngleVelocityX(float speed) {
    setAngleVelocityX(speed, FOREVER);
  }

  /**
   * Sets the angular speed of the y axis.
   */
  public void setAngleVelocityY(float speed) {
    setAngleVelocityY(speed, FOREVER);
  }

  /**
   * Sets the angular speed of the z axis.
   */
  public void setAngleVelocityZ(float speed) {
    setAngleVelocityZ(speed, FOREVER);
  }

  /**
   * Sets the angular speed of the x axis over the specified time.
   */
  public void setAngleVelocityX(float speed, long time) {
    velocityAngleX.set(speed, time);
  }

  /**
   * Sets the angular speed of the y axis over the specified time.
   */
  public void setAngleVelocityY(float speed, long time) {
    velocityAngleY.set(speed, time);
  }

  /**
   * Sets the angular speed of the z axis over the specified time.
   */
  public void setAngleVelocityZ(float speed, long time) {
    velocityAngleZ.set(speed, time);
  }

  /**
   * Sets the angular speed of the x axis over the specified time.
   */
  public float getAngleVelocityX() {
    return isTurningX() ? velocityAngleX.speed : 0;
  }

  /**
   * Sets the angular speed of the y axis over the specified time.
   */
  public float getAngleVelocityY() {
    return isTurningY() ? velocityAngleY.speed : 0;
  }

  /**
   * Sets the angular speed of the z axis over the specified time.
   */
  public float getAngleVelocityZ() {
    return isTurningZ() ? velocityAngleZ.speed : 0;
  }

  /**
   * Returns true if the x axis is currently turning.
   */
  public boolean isTurningX() {
    return !velocityAngleX.isStopped();
  }

  /**
   * Returns true if the y axis is currently turning.
   */
  public boolean isTurningY() {
    return !velocityAngleY.isStopped();
  }

  /**
   * Returns true if the z axis is currently turning.
   */
  public boolean isTurningZ() {
    return !velocityAngleZ.isStopped();
  }

  /**
   * The Movement class contains a speed and an amount of time to continue
   * that speed.
   */
  protected static class Movement {
    // change per millisecond
    float speed;

    long remainingTime;

    /**
     * Sets this movement to the specified speed and time (in milliseconds).
     */
    public void set(float speed, long time) {
      this.speed = speed;
      this.remainingTime = time;
    }

    public boolean isStopped() {
      return (speed == 0) || (remainingTime == 0);
    }

    /**
     * Gets the distance traveled in the specified amount of time in
     * milliseconds.
     */
    public float getDistance(long elapsedTime) {
      if (remainingTime == 0) {
        return 0;
      } else if (remainingTime != FOREVER) {
        elapsedTime = Math.min(elapsedTime, remainingTime);
        remainingTime -= elapsedTime;
      }
      return speed * elapsedTime;
    }
  }
}

/**
 * A BSPTreeTraverseListener is an interface for a BSPTreeTraverser to signal
 * visited polygons.
 */

interface BSPTreeTraverseListener {

  /**
   * Visits a BSP polygon. Called by a BSPTreeTraverer. If this method returns
   * true, the BSPTreeTraverer will stop the current traversal. Otherwise, the
   * BSPTreeTraverer will continue if there are polygons in the tree that have
   * not yet been traversed.
   */
  public boolean visitPolygon(BSPPolygon poly, boolean isBackLeaf);

}

/**
 * The BSPTreeBuilder class builds a BSP tree from a list of polygons. The
 * polygons must be BSPPolygons.
 * 
 * Currently, the builder does not try to optimize the order of the partitions,
 * and could be optimized by choosing partitions in an order that minimizes
 * polygon splits and provides a more balanced, complete tree.
 */

class BSPTreeBuilder {

  /**
   * The bsp tree currently being built.
   */
  protected BSPTree currentTree;

  /**
   * Builds a BSP tree.
   */
  public BSPTree build(List polygons) {
    currentTree = new BSPTree(createNewNode(polygons));
    buildNode(currentTree.getRoot());
    return currentTree;
  }

  /**
   * Builds a node in the BSP tree.
   */
  protected void buildNode(BSPTree.Node node) {

    // nothing to build if it's a leaf
    if (node instanceof BSPTree.Leaf) {
      return;
    }

    // classify all polygons relative to the partition
    // (front, back, or collinear)
    ArrayList collinearList = new ArrayList();
    ArrayList frontList = new ArrayList();
    ArrayList backList = new ArrayList();
    List allPolygons = node.polygons;
    node.polygons = null;
    for (int i = 0; i < allPolygons.size(); i++) {
      BSPPolygon poly = (BSPPolygon) allPolygons.get(i);
      int side = node.partition.getSide(poly);
      if (side == BSPLine.COLLINEAR) {
        collinearList.add(poly);
      } else if (side == BSPLine.FRONT) {
        frontList.add(poly);
      } else if (side == BSPLine.BACK) {
        backList.add(poly);
      } else if (side == BSPLine.SPANNING) {
        BSPPolygon front = clipBack(poly, node.partition);
        BSPPolygon back = clipFront(poly, node.partition);
        if (front != null) {
          frontList.add(front);
        }
        if (back != null) {
          backList.add(back);
        }

      }
    }

    // clean and assign lists
    collinearList.trimToSize();
    frontList.trimToSize();
    backList.trimToSize();
    node.polygons = collinearList;
    node.front = createNewNode(frontList);
    node.back = createNewNode(backList);

    // build front and back nodes
    buildNode(node.front);
    buildNode(node.back);
    if (node.back instanceof BSPTree.Leaf) {
      ((BSPTree.Leaf) node.back).isBack = true;
    }
  }

  /**
   * Creates a new node from a list of polygons. If none of the polygons are
   * walls, a leaf is created.
   */
  protected BSPTree.Node createNewNode(List polygons) {

    BSPLine partition = choosePartition(polygons);

    // no partition available, so it's a leaf
    if (partition == null) {
      BSPTree.Leaf leaf = new BSPTree.Leaf();
      leaf.polygons = polygons;
      buildLeaf(leaf);
      return leaf;
    } else {
      BSPTree.Node node = new BSPTree.Node();
      node.polygons = polygons;
      node.partition = partition;
      return node;
    }
  }

  /**
   * Builds a leaf in the tree, calculating extra information like leaf
   * bounds, floor height, and ceiling height.
   */
  protected void buildLeaf(BSPTree.Leaf leaf) {

    if (leaf.polygons.size() == 0) {
      // leaf represents an empty space
      leaf.ceilHeight = Float.MAX_VALUE;
      leaf.floorHeight = Float.MIN_VALUE;
      leaf.bounds = null;
      return;
    }

    float minX = Float.MAX_VALUE;
    float maxX = Float.MIN_VALUE;
    float minY = Float.MAX_VALUE;
    float maxY = Float.MIN_VALUE;
    float minZ = Float.MAX_VALUE;
    float maxZ = Float.MIN_VALUE;

    // find min y, max y, and bounds
    Iterator i = leaf.polygons.iterator();
    while (i.hasNext()) {
      BSPPolygon poly = (BSPPolygon) i.next();
      for (int j = 0; j < poly.getNumVertices(); j++) {
        Vector3D v = poly.getVertex(j);
        minX = Math.min(minX, v.x);
        maxX = Math.max(maxX, v.x);
        minY = Math.min(minY, v.y);
        maxY = Math.max(maxY, v.y);
        minZ = Math.min(minZ, v.z);
        maxZ = Math.max(maxZ, v.z);
      }
    }

    // find any platform within the leaf
    i = leaf.polygons.iterator();
    while (i.hasNext()) {
      BSPPolygon poly = (BSPPolygon) i.next();
      // if a floor
      if (poly.getNormal().y == 1) {
        float y = poly.getVertex(0).y;
        if (y > minY && y < maxY) {
          minY = y;
        }
      }
    }

    // set the leaf values
    leaf.ceilHeight = maxY;
    leaf.floorHeight = minY;
    leaf.bounds = new Rectangle((int) Math.floor(minX), (int) Math
        .floor(minZ), (int) Math.ceil(maxX - minX + 1), (int) Math
        .ceil(maxZ - minZ + 1));
  }

  /**
   * Chooses a line from a list of polygons to use as a partition. This method
   * just returns the line formed by the first vertical polygon, or null if
   * none found. A smarter method would choose a partition that minimizes
   * polygon splits and provides a more balanced, complete tree.
   */
  protected BSPLine choosePartition(List polygons) {
    for (int i = 0; i < polygons.size(); i++) {
      BSPPolygon poly = (BSPPolygon) polygons.get(i);
      if (poly.isWall()) {
        return new BSPLine(poly);
      }
    }
    return null;
  }

  /**
   * Clips away the part of the polygon that lines in front of the specified
   * line. The returned polygon is the part of the polygon in back of the
   * line. Returns null if the line does not split the polygon. The original
   * polygon is untouched.
   */
  protected BSPPolygon clipFront(BSPPolygon poly, BSPLine line) {
    return clip(poly, line, BSPLine.FRONT);
  }

  /**
   * Clips away the part of the polygon that lines in back of the specified
   * line. The returned polygon is the part of the polygon in front of the
   * line. Returns null if the line does not split the polygon. The original
   * polygon is untouched.
   */
  protected BSPPolygon clipBack(BSPPolygon poly, BSPLine line) {
    return clip(poly, line, BSPLine.BACK);
  }

  /**
   * Clips a BSPPolygon so that the part of the polygon on the specified side
   * (either BSPLine.FRONT or BSPLine.BACK) is removed, and returnes the
   * clipped polygon. Returns null if the line does not split the polygon. The
   * original polygon is untouched.
   */
  protected BSPPolygon clip(BSPPolygon poly, BSPLine line, int clipSide) {
    ArrayList vertices = new ArrayList();
    BSPLine polyEdge = new BSPLine();

    // add vertices that aren't on the clip side
    Point2D.Float intersection = new Point2D.Float();
    for (int i = 0; i < poly.getNumVertices(); i++) {
      int next = (i + 1) % poly.getNumVertices();
      Vector3D v1 = poly.getVertex(i);
      Vector3D v2 = poly.getVertex(next);
      int side1 = line.getSideThin(v1.x, v1.z);
      int side2 = line.getSideThin(v2.x, v2.z);
      if (side1 != clipSide) {
        vertices.add(v1);
      }

      if ((side1 == BSPLine.FRONT && side2 == BSPLine.BACK)
          || (side2 == BSPLine.FRONT && side1 == BSPLine.BACK)) {
        // ensure v1.z < v2.z
        if (v1.z > v2.z) {
          Vector3D temp = v1;
          v1 = v2;
          v2 = temp;
        }
        polyEdge.setLine(v1.x, v1.z, v2.x, v2.z);
        float f = polyEdge.getIntersection(line);
        Vector3D tPoint = new Vector3D(v1.x + f * (v2.x - v1.x), v1.y
            + f * (v2.y - v1.y), v1.z + f * (v2.z - v1.z));
        vertices.add(tPoint);
        // remove any created t-junctions
        removeTJunctions(v1, v2, tPoint);
      }

    }

    // Remove adjacent equal vertices. (A->A) becomes (A)
    for (int i = 0; i < vertices.size(); i++) {
      Vector3D v = (Vector3D) vertices.get(i);
      Vector3D next = (Vector3D) vertices.get((i + 1) % vertices.size());
      if (v.equals(next)) {
        vertices.remove(i);
        i--;
      }
    }

    if (vertices.size() < 3) {
      return null;
    }

    // make the polygon
    Vector3D[] array = new Vector3D[vertices.size()];
    vertices.toArray(array);
    return poly.clone(array);
  }

  /**
   * Remove any T-Junctions from the current tree along the line specified by
   * (v1, v2). Find all polygons with this edge and insert the T-intersection
   * point between them.
   */
  protected void removeTJunctions(final Vector3D v1, final Vector3D v2,
      final Vector3D tPoint) {
    BSPTreeTraverser traverser = new BSPTreeTraverser(
        new BSPTreeTraverseListener() {
          public boolean visitPolygon(BSPPolygon poly,
              boolean isBackLeaf) {
            removeTJunctions(poly, v1, v2, tPoint);
            return true;
          }
        });
    traverser.traverse(currentTree);
  }

  /**
   * Remove any T-Junctions from the specified polygon. The T-intersection
   * point is inserted between the points v1 and v2 if there are no other
   * points between them.
   */
  protected void removeTJunctions(BSPPolygon poly, Vector3D v1, Vector3D v2,
      Vector3D tPoint) {
    for (int i = 0; i < poly.getNumVertices(); i++) {
      int next = (i + 1) % poly.getNumVertices();
      Vector3D p1 = poly.getVertex(i);
      Vector3D p2 = poly.getVertex(next);
      if ((p1.equals(v1) && p2.equals(v2))
          || (p1.equals(v2) && p2.equals(v1))) {
        poly.insertVertex(next, tPoint);
        return;
      }
    }
  }

}

/**
 * The TexturedPolygon3D class is a Polygon with a texture.
 */

class TexturedPolygon3D extends Polygon3D {

  protected Rectangle3D textureBounds;

  protected Texture texture;

  public TexturedPolygon3D() {
    textureBounds = new Rectangle3D();
  }

  public TexturedPolygon3D(Vector3D v0, Vector3D v1, Vector3D v2) {
    this(new Vector3D[] { v0, v1, v2 });
  }

  public TexturedPolygon3D(Vector3D v0, Vector3D v1, Vector3D v2, Vector3D v3) {
    this(new Vector3D[] { v0, v1, v2, v3 });
  }

  public TexturedPolygon3D(Vector3D[] vertices) {
    super(vertices);
    textureBounds = new Rectangle3D();
  }

  public void setTo(Polygon3D poly) {
    super.setTo(poly);
    if (poly instanceof TexturedPolygon3D) {
      TexturedPolygon3D tPoly = (TexturedPolygon3D) poly;
      textureBounds.setTo(tPoly.textureBounds);
      texture = tPoly.texture;
    }
  }

  /**
   * Gets this polygon's texture.
   */
  public Texture getTexture() {
    return texture;
  }

  /**
   * Gets this polygon's texture bounds.
   */
  public Rectangle3D getTextureBounds() {
    return textureBounds;
  }

  /**
   * Sets this polygon's texture.
   */
  public void setTexture(Texture texture) {
    this.texture = texture;
    textureBounds.setWidth(texture.getWidth());
    textureBounds.setHeight(texture.getHeight());
  }

  /**
   * Sets this polygon's texture and texture bounds.
   */
  public void setTexture(Texture texture, Rectangle3D bounds) {
    setTexture(texture);
    textureBounds.setTo(bounds);
  }

  public void add(Vector3D u) {
    super.add(u);
    textureBounds.add(u);
  }

  public void subtract(Vector3D u) {
    super.subtract(u);
    textureBounds.subtract(u);
  }

  public void addRotation(Transform3D xform) {
    super.addRotation(xform);
    textureBounds.addRotation(xform);
  }

  public void subtractRotation(Transform3D xform) {
    super.subtractRotation(xform);
    textureBounds.subtractRotation(xform);
  }

  /**
   * Calculates the bounding rectangle for this polygon that is aligned with
   * the texture bounds.
   */
  public Rectangle3D calcBoundingRectangle() {

    Vector3D u = new Vector3D(textureBounds.getDirectionU());
    Vector3D v = new Vector3D(textureBounds.getDirectionV());
    Vector3D d = new Vector3D();
    u.normalize();
    v.normalize();

    float uMin = 0;
    float uMax = 0;
    float vMin = 0;
    float vMax = 0;
    for (int i = 0; i < getNumVertices(); i++) {
      d.setTo(getVertex(i));
      d.subtract(getVertex(0));
      float uLength = d.getDotProduct(u);
      float vLength = d.getDotProduct(v);
      uMin = Math.min(uLength, uMin);
      uMax = Math.max(uLength, uMax);
      vMin = Math.min(vLength, vMin);
      vMax = Math.max(vLength, vMax);
    }

    Rectangle3D boundingRect = new Rectangle3D();
    Vector3D origin = boundingRect.getOrigin();
    origin.setTo(getVertex(0));
    d.setTo(u);
    d.multiply(uMin);
    origin.add(d);
    d.setTo(v);
    d.multiply(vMin);
    origin.add(d);
    boundingRect.getDirectionU().setTo(u);
    boundingRect.getDirectionV().setTo(v);
    boundingRect.setWidth(uMax - uMin);
    boundingRect.setHeight(vMax - vMin);

    // explictly set the normal since the texture directions
    // could create a normal negative to the polygon normal
    boundingRect.setNormal(getNormal());

    return boundingRect;
  }

}

/**
 * A BSPPolygon is a TexturedPolygon3D with a type (TYPE_FLOOR, TYPE_WALL, or
 * TYPE_PASSABLE_WALL) an ambient light intensity value, and a BSPLine
 * representation if the type is a TYPE_WALL or TYPE_PASSABLE_WALL.
 */

class BSPPolygon extends TexturedPolygon3D {

  public static final int TYPE_FLOOR = 0;

  public static final int TYPE_WALL = 1;

  public static final int TYPE_PASSABLE_WALL = 2;

  /**
   * How short a wall must be so that monsters/players can step over it.
   */
  public static final int PASSABLE_WALL_THRESHOLD = 32;

  /**
   * How tall an entryway must be so that monsters/players can pass through it
   */
  public static final int PASSABLE_ENTRYWAY_THRESHOLD = 128;

  private int type;

  private float ambientLightIntensity;

  private BSPLine line;

  /**
   * Creates a new BSPPolygon with the specified vertices and type
   * (TYPE_FLOOR, TYPE_WALL, or TYPE_PASSABLE_WALL).
   */
  public BSPPolygon(Vector3D[] vertices, int type) {
    super(vertices);
    this.type = type;
    ambientLightIntensity = 0.5f;
    if (isWall()) {
      line = new BSPLine(this);
    }
  }

  /**
   * Clone this polygon, but with a different set of vertices.
   */
  public BSPPolygon clone(Vector3D[] vertices) {
    BSPPolygon clone = new BSPPolygon(vertices, type);
    clone.setNormal(getNormal());
    clone.setAmbientLightIntensity(getAmbientLightIntensity());
    if (getTexture() != null) {
      clone.setTexture(getTexture(), getTextureBounds());
    }
    return clone;
  }

  /**
   * Returns true if the BSPPolygon is a wall.
   */
  public boolean isWall() {
    return (type == TYPE_WALL) || (type == TYPE_PASSABLE_WALL);
  }

  /**
   * Returns true if the BSPPolygon is a solid wall (not passable).
   */
  public boolean isSolidWall() {
    return type == TYPE_WALL;
  }

  /**
   * Gets the line representing the BSPPolygon. Returns null if this
   * BSPPolygon is not a wall.
   */
  public BSPLine getLine() {
    return line;
  }

  public void setAmbientLightIntensity(float a) {
    ambientLightIntensity = a;
  }

  public float getAmbientLightIntensity() {
    return ambientLightIntensity;
  }

}
/**
 * The RoomDef class represents a convex room with walls, a floor, and a
 * ceiling. The floor may be above the ceiling, in which case the RoomDef is a
 * "pillar" or "block" structure, rather than a "room". RoomDefs are used as a
 * shortcut to create the actual BSPPolygons used in the 2D BSP tree.
 */

class RoomDef {

  private static final Vector3D FLOOR_NORMAL = new Vector3D(0, 1, 0);

  private static final Vector3D CEIL_NORMAL = new Vector3D(0, -1, 0);

  private HorizontalAreaDef floor;

  private HorizontalAreaDef ceil;

  private List vertices;

  private float ambientLightIntensity;

  /**
   * The HorizontalAreaDef class represents a floor or ceiling.
   */
  private static class HorizontalAreaDef {
    float height;

    Texture texture;

    Rectangle3D textureBounds;

    public HorizontalAreaDef(float height, Texture texture,
        Rectangle3D textureBounds) {
      this.height = height;
      this.texture = texture;
      this.textureBounds = textureBounds;
    }
  }

  /**
   * The Vertex class represents a Wall vertex.
   */
  private static class Vertex {
    float x;

    float z;

    float bottom;

    float top;

    Texture texture;

    Rectangle3D textureBounds;

    public Vertex(float x, float z, float bottom, float top,
        Texture texture, Rectangle3D textureBounds) {
      this.x = x;
      this.z = z;
      this.bottom = bottom;
      this.top = top;
      this.texture = texture;
      this.textureBounds = textureBounds;
    }

    public boolean isWall() {
      return (bottom != top) && (texture != null);
    }
  }

  /**
   * Creates a new RoomDef with an ambient light intensity of 0.5. The walls,
   * floors and ceiling all use this ambient light intensity.
   */
  public RoomDef() {
    this(0.5f);
  }

  /**
   * Creates a new RoomDef with the specified ambient light intensity. The
   * walls, floors and ceiling all use this ambient light intensity.
   */
  public RoomDef(float ambientLightIntensity) {
    this.ambientLightIntensity = ambientLightIntensity;
    vertices = new ArrayList();
  }

  /**
   * Adds a new wall vertex at the specified (x,z) location, with the
   * specified texture. The wall stretches from the floor to the ceiling. If
   * the texture is null, no polygon for the wall is created.
   */
  public void addVertex(float x, float z, Texture texture) {
    addVertex(x, z, Math.min(floor.height, ceil.height), Math.max(
        floor.height, ceil.height), texture);
  }

  /**
   * Adds a new wall vertex at the specified (x,z) location, with the
   * specified texture, bottom location, and top location. If the texture is
   * null, no polygon for the wall is created.
   */
  public void addVertex(float x, float z, float bottom, float top,
      Texture texture) {
    vertices.add(new Vertex(x, z, bottom, top, texture, null));
  }

  /**
   * Adds a new wall vertex at the specified (x,z) location, with the
   * specified texture, texture bounds, bottom location, and top location. If
   * the texture is null, no polygon for the wall is created.
   */
  public void addVertex(float x, float z, float bottom, float top,
      Texture texture, Rectangle3D texBounds) {
    vertices.add(new Vertex(x, z, bottom, top, texture, texBounds));
  }

  /**
   * Sets the floor height and floor texture of this room. If the texture is
   * null, no floor polygon is created, but the height of the floor is used as
   * the default bottom wall boundary.
   */
  public void setFloor(float height, Texture texture) {
    setFloor(height, texture, null);
  }

  /**
   * Sets the floor height, floor texture, and floor texture bounds of this
   * room. If the texture is null, no floor polygon is created, but the height
   * of the floor is used as the default bottom wall boundary. If the texture
   * bounds is null, a default texture bounds is used.
   */
  public void setFloor(float height, Texture texture, Rectangle3D texBounds) {
    if (texture != null && texBounds == null) {
      texBounds = new Rectangle3D(new Vector3D(0, height, 0),
          new Vector3D(1, 0, 0), new Vector3D(0, 0, -1), texture
              .getWidth(), texture.getHeight());
    }
    floor = new HorizontalAreaDef(height, texture, texBounds);
  }

  /**
   * Sets the ceiling height and ceiling texture of this room. If the texture
   * is null, no ceiling polygon is created, but the height of the ceiling is
   * used as the default top wall boundary.
   */
  public void setCeil(float height, Texture texture) {
    setCeil(height, texture, null);
  }

  /**
   * Sets the ceiling height, ceiling texture, and ceiling texture bounds of
   * this room. If the texture is null, no floor polygon is created, but the
   * height of the floor is used as the default bottom wall boundary. If the
   * texture bounds is null, a default texture bounds is used.
   */
  public void setCeil(float height, Texture texture, Rectangle3D texBounds) {
    if (texture != null && texBounds == null) {
      texBounds = new Rectangle3D(new Vector3D(0, height, 0),
          new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), texture
              .getWidth(), texture.getHeight());
    }
    ceil = new HorizontalAreaDef(height, texture, texBounds);
  }

  /**
   * Creates and returns a list of BSPPolygons that represent the walls,
   * floor, and ceiling of this room.
   */
  public List createPolygons() {
    List walls = createVerticalPolygons();
    List floors = createHorizontalPolygons();

    List list = new ArrayList(walls.size() + floors.size());
    list.addAll(walls);
    list.addAll(floors);
    return list;
  }

  /**
   * Creates and returns a list of BSPPolygons that represent the vertical
   * walls of this room.
   */
  public List createVerticalPolygons() {
    int size = vertices.size();
    List list = new ArrayList(size);
    if (size == 0) {
      return list;
    }
    Vertex origin = (Vertex) vertices.get(0);
    Vector3D textureOrigin = new Vector3D(origin.x, ceil.height, origin.z);
    Vector3D textureDy = new Vector3D(0, -1, 0);

    for (int i = 0; i < size; i++) {
      Vertex curr = (Vertex) vertices.get(i);

      if (!curr.isWall()) {
        continue;
      }

      // determine if wall is passable (useful for portals)
      int type = BSPPolygon.TYPE_WALL;
      if (floor.height > ceil.height) {
        if (floor.height - ceil.height <= BSPPolygon.PASSABLE_WALL_THRESHOLD) {
          type = BSPPolygon.TYPE_PASSABLE_WALL;
        }
      } else if (curr.top - curr.bottom <= BSPPolygon.PASSABLE_WALL_THRESHOLD) {
        type = BSPPolygon.TYPE_PASSABLE_WALL;
      } else if (curr.bottom - floor.height >= BSPPolygon.PASSABLE_ENTRYWAY_THRESHOLD) {
        type = BSPPolygon.TYPE_PASSABLE_WALL;
      }

      List wallVertices = new ArrayList();
      Vertex prev;
      Vertex next;
      if (floor.height < ceil.height) {
        prev = (Vertex) vertices.get((i + size - 1) % size);
        next = (Vertex) vertices.get((i + 1) % size);
      } else {
        prev = (Vertex) vertices.get((i + 1) % size);
        next = (Vertex) vertices.get((i + size - 1) % size);
      }

      // bottom vertices
      wallVertices.add(new Vector3D(next.x, curr.bottom, next.z));
      wallVertices.add(new Vector3D(curr.x, curr.bottom, curr.z));

      // optional vertices at T-Junctions on left side
      if (prev.isWall()) {
        if (prev.bottom > curr.bottom && prev.bottom < curr.top) {
          wallVertices.add(new Vector3D(curr.x, prev.bottom, curr.z));
        }
        if (prev.top > curr.bottom && prev.top < curr.top) {
          wallVertices.add(new Vector3D(curr.x, prev.top, curr.z));
        }

      }

      // top vertives
      wallVertices.add(new Vector3D(curr.x, curr.top, curr.z));
      wallVertices.add(new Vector3D(next.x, curr.top, next.z));

      // optional vertices at T-Junctions on left side
      if (next.isWall()) {
        if (next.top > curr.bottom && next.top < curr.top) {
          wallVertices.add(new Vector3D(next.x, next.top, next.z));
        }
        if (next.bottom > curr.bottom && next.bottom < curr.top) {
          wallVertices.add(new Vector3D(next.x, next.bottom, next.z));
        }

      }

      // create wall polygon
      Vector3D[] array = new Vector3D[wallVertices.size()];
      wallVertices.toArray(array);
      BSPPolygon poly = new BSPPolygon(array, type);
      poly.setAmbientLightIntensity(ambientLightIntensity);
      if (curr.textureBounds == null) {
        Vector3D textureDx = new Vector3D(next.x, 0, next.z);
        textureDx.subtract(new Vector3D(curr.x, 0, curr.z));
        textureDx.normalize();
        curr.textureBounds = new Rectangle3D(textureOrigin, textureDx,
            textureDy, curr.texture.getWidth(), curr.texture
                .getHeight());
      }
      poly.setTexture(curr.texture, curr.textureBounds);
      list.add(poly);
    }
    return list;
  }

  /**
   * Creates and returns a list of BSPPolygons that represent the horizontal
   * floor and ceiling of this room.
   */
  public List createHorizontalPolygons() {

    List list = new ArrayList(2);
    int size = vertices.size();
    Vector3D[] floorVertices = new Vector3D[size];
    Vector3D[] ceilVertices = new Vector3D[size];

    // create vertices
    for (int i = 0; i < size; i++) {
      Vertex v = (Vertex) vertices.get(i);
      floorVertices[i] = new Vector3D(v.x, floor.height, v.z);
      ceilVertices[size - (i + 1)] = new Vector3D(v.x, ceil.height, v.z);
    }

    // create floor polygon
    if (floor.texture != null) {
      BSPPolygon poly = new BSPPolygon(floorVertices,
          BSPPolygon.TYPE_FLOOR);
      poly.setTexture(floor.texture, floor.textureBounds);
      poly.setNormal(FLOOR_NORMAL);
      poly.setAmbientLightIntensity(ambientLightIntensity);
      list.add(poly);
    }

    // create ceiling polygon
    if (ceil.texture != null) {
      BSPPolygon poly = new BSPPolygon(ceilVertices,
          BSPPolygon.TYPE_FLOOR);
      poly.setTexture(ceil.texture, ceil.textureBounds);
      poly.setNormal(CEIL_NORMAL);
      poly.setAmbientLightIntensity(ambientLightIntensity);
      list.add(poly);
    }

    return list;
  }

}

/**
 * The MapLoader class loads maps from a text file based on the Alias|Wavefront
 * OBJ file specification.
 * 
 * MAP file commands:
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      
 *       
 *        
 *         
 *          
 *           
 *            
 *             
 *              
 *               
 *                
 *                 
 *                  
 *                   
 *                    
 *                     
 *                      
 *                       v [x] [y] [z]        - Define a vertex with floating-point
 *                       coords (x,y,z).
 *                       mtllib [filename]    - Load materials from an external .mtl
 *                       file.
 *                       usemtl [name]        - Use the named material (loaded from a
 *                       .mtl file) for the next floor, ceiling,
 *                       or wall.
 *                       ambientLightIntensity
 *                       [value]          - Defines the ambient light intensity
 *                       for the next room, from 0 to 1.
 *                       pointlight [v]       - Defines a point light located at the
 *                       [intensity]        specfied vector. Optionally, light
 *                       [falloff]          intesity and falloff distance can
 *                       be specified.
 *                       player [v] [angle]   - Specifies the starting location of the
 *                       player and optionally a starting
 *                       angle, in radians, around the y-axis.
 *                       obj [uniqueName]     - Defines an object from an external
 *                       [filename] [v]     OBJ file. The unique name allows this
 *                       [angle]            object to be uniquely identfied, but
 *                       can be &quot;null&quot; if no unique name is
 *                       needed. The filename is an external
 *                       OBJ file. Optionally, the starting
 *                       angle, in radians, around the y-axis
 *                       can be specified.
 *                       room [name]          - Defines a new room, optionally giving
 *                       the room a name. A room consists of
 *                       vertical walls, a horizontal floor
 *                       and a horizontal ceiling. Concave rooms
 *                       are currently not supported, but can be
 *                       simulated by adjacent convex rooms.
 *                       floor [height]       - Defines the height of the floor of
 *                       the current room, using the current
 *                       material. The current material can
 *                       be null, in which case no floor
 *                       polygon is created. The floor can be
 *                       above the ceiling, in which case a
 *                       &quot;pillar&quot; or &quot;block&quot; structure is
 *                       created, rather than a &quot;room&quot;.
 *                       ceil [height]        - Defines the height of the ceiling of
 *                       the current room, using the current
 *                       material. The current material can
 *                       be null, in which case no ceiling
 *                       polygon is created. The ceiling can be
 *                       below the floor, in which case a
 *                       &quot;pillar&quot; or &quot;block&quot; structure is
 *                       created, rather than a &quot;room&quot;.
 *                       wall [x] [z]         - Defines a wall vertex in a room using
 *                       [bottom] [top]    the specified x and z coordinates.
 *                       Walls should be defined in clockwise
 *                       order. If &quot;bottom&quot; and &quot;top&quot; is not
 *                       defined, the floor and ceiling height
 *                       are used. If the current material is
 *                       null, or bottom is equal to top, no
 *                       wall polygon is created.
 *                       
 *                      
 *                     
 *                    
 *                   
 *                  
 *                 
 *                
 *               
 *              
 *             
 *            
 *           
 *          
 *         
 *        
 *       
 *      
 *     
 *    
 *   
 *  
 * </pre>
 */

class MapLoader extends ObjectLoader {

  private BSPTreeBuilder builder;

  private Map loadedObjects;

  private Transform3D playerStart;

  private RoomDef currentRoom;

  private List rooms;

  private List mapObjects;

  // use a separate ObjectLoader for objects
  private ObjectLoader objectLoader;

  /**
   * Creates a new MapLoader using the default BSPTreeBuilder.
   */
  public MapLoader() {
    this(null);
  }

  /**
   * Creates a new MapLoader using the specified BSPTreeBuilder. If the
   * builder is null, a default BSPTreeBuilder is created.
   */
  public MapLoader(BSPTreeBuilder builder) {
    if (builder == null) {
      this.builder = new BSPTreeBuilder();
    } else {
      this.builder = builder;
    }
    parsers.put("map", new MapLineParser());
    objectLoader = new ObjectLoader();
    loadedObjects = new HashMap();
    rooms = new ArrayList();
    mapObjects = new ArrayList();
  }

  /**
   * Loads a map file and creates a BSP tree. Objects created can be retrieved
   * from the getObjectsInMap() method.
   */
  public BSPTree loadMap(String filename) throws IOException {
    currentRoom = null;
    rooms.clear();
    vertices.clear();
    mapObjects.clear();
    playerStart = new Transform3D();

    path = new File(filename).getParentFile();

    parseFile(filename);

    return createBSPTree();
  }

  /**
   * Creates a BSP tree from the rooms defined in the map file.
   */
  protected BSPTree createBSPTree() {
    // extract all polygons
    List allPolygons = new ArrayList();
    for (int i = 0; i < rooms.size(); i++) {
      RoomDef room = (RoomDef) rooms.get(i);
      allPolygons.addAll(room.createPolygons());
    }

    // build the tree
    BSPTree tree = builder.build(allPolygons);

    // create polygon surfaces based on the lights.
    tree.createSurfaces(lights);
    return tree;
  }

  /**
   * Gets a list of all objects degined in the map file.
   */
  public List getObjectsInMap() {
    return mapObjects;
  }

  /**
   * Gets the player start location defined in the map file.
   */
  public Transform3D getPlayerStartLocation() {
    return playerStart;
  }

  /**
   * Sets the lights used for OBJ objects.
   */
  public void setObjectLights(List lights, float ambientLightIntensity) {
    objectLoader.setLights(lights, ambientLightIntensity);
  }

  /**
   * Parses a line in a MAP file.
   */
  protected class MapLineParser implements LineParser {

    public void parseLine(String line) throws IOException,
        NoSuchElementException {
      StringTokenizer tokenizer = new StringTokenizer(line);
      String command = tokenizer.nextToken();

      if (command.equals("v")) {
        // create a new vertex
        vertices.add(new Vector3D(Float.parseFloat(tokenizer
            .nextToken()), Float.parseFloat(tokenizer.nextToken()),
            Float.parseFloat(tokenizer.nextToken())));
      } else if (command.equals("mtllib")) {
        // load materials from file
        String name = tokenizer.nextToken();
        parseFile(name);
      } else if (command.equals("usemtl")) {
        // define the current material
        String name = tokenizer.nextToken();
        if ("null".equals(name)) {
          currentMaterial = new Material();
        } else {
          currentMaterial = (Material) materials.get(name);
          if (currentMaterial == null) {
            currentMaterial = new Material();
            System.out.println("no material: " + name);
          }
        }
      } else if (command.equals("pointlight")) {
        // create a point light
        Vector3D loc = getVector(tokenizer.nextToken());
        float intensity = 1;
        float falloff = PointLight3D.NO_DISTANCE_FALLOFF;
        if (tokenizer.hasMoreTokens()) {
          intensity = Float.parseFloat(tokenizer.nextToken());
        }
        if (tokenizer.hasMoreTokens()) {
          falloff = Float.parseFloat(tokenizer.nextToken());
        }
        lights.add(new PointLight3D(loc.x, loc.y, loc.z, intensity,
            falloff));
      } else if (command.equals("ambientLightIntensity")) {
        // define the ambient light intensity
        ambientLightIntensity = Float.parseFloat(tokenizer.nextToken());
      } else if (command.equals("player")) {
        // define the player start location
        playerStart.getLocation().setTo(
            getVector(tokenizer.nextToken()));
        if (tokenizer.hasMoreTokens()) {
          playerStart.setAngleY(Float.parseFloat(tokenizer
              .nextToken()));
        }
      } else if (command.equals("obj")) {
        // create a new obj from an object file
        String uniqueName = tokenizer.nextToken();
        String filename = tokenizer.nextToken();
        // check if the object is already loaded
        PolygonGroup object = (PolygonGroup) loadedObjects
            .get(filename);
        if (object == null) {
          File file = new File(path, filename);
          String filePath = file.getPath();
          object = objectLoader.loadObject(filePath);
          loadedObjects.put(filename, object);
        }
        Vector3D loc = getVector(tokenizer.nextToken());
        PolygonGroup mapObject = (PolygonGroup) object.clone();
        mapObject.getTransform().getLocation().setTo(loc);
        if (!uniqueName.equals("null")) {
          mapObject.setName(uniqueName);
        }
        if (tokenizer.hasMoreTokens()) {
          mapObject.getTransform().setAngleY(
              Float.parseFloat(tokenizer.nextToken()));
        }
        mapObjects.add(mapObject);
      } else if (command.equals("room")) {
        // start a new room
        currentRoom = new RoomDef(ambientLightIntensity);
        rooms.add(currentRoom);
      } else if (command.equals("floor")) {
        // define a room's floor
        float y = Float.parseFloat(tokenizer.nextToken());
        currentRoom.setFloor(y, currentMaterial.texture);
      } else if (command.equals("ceil")) {
        // define a room's ceiling
        float y = Float.parseFloat(tokenizer.nextToken());
        currentRoom.setCeil(y, currentMaterial.texture);
      } else if (command.equals("wall")) {
        // define a wall vertex in a room.
        float x = Float.parseFloat(tokenizer.nextToken());
        float z = Float.parseFloat(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) {
          float bottom = Float.parseFloat(tokenizer.nextToken());
          float top = Float.parseFloat(tokenizer.nextToken());
          currentRoom.addVertex(x, z, bottom, top,
              currentMaterial.texture);
        } else {
          currentRoom.addVertex(x, z, currentMaterial.texture);
        }
      } else {
        System.out.println("Unknown command: " + command);
      }
    }
  }
}
/**
 * The ObjectLoader class loads a subset of the Alias|Wavefront OBJ file
 * specification.
 * 
 * Lines that begin with '#' are comments.
 * 
 * OBJ file keywords:
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      mtllib [filename]    - Load materials from an external .mtl
 *      file.
 *      v [x] [y] [z]        - Define a vertex with floating-point
 *      coords (x,y,z).
 *      f [v1] [v2] [v3] ... - Define a new face. a face is a flat,
 *      convex polygon with vertices in
 *      counter-clockwise order. Positive
 *      numbers indicate the index of the
 *      vertex that is defined in the file.
 *      Negative numbers indicate the vertex
 *      defined relative to last vertex read.
 *      For example, 1 indicates the first
 *      vertex in the file, -1 means the last
 *      vertex read, and -2 is the vertex
 *      before that.
 *      g [name]             - Define a new group by name. The faces
 *      following are added to this group.
 *      usemtl [name]        - Use the named material (loaded from a
 *      .mtl file) for the faces in this group.
 *      
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * MTL file keywords:
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      newmtl [name]        - Define a new material by name.
 *      map_Kd [filename]    - Give the material a texture map.
 *      
 *     
 *    
 *   
 *  
 * </pre>
 */

class ObjectLoader {

  /**
   * The Material class wraps a ShadedTexture.
   */
  public static class Material {
    public File sourceFile;

    public ShadedTexture texture;
  }

  /**
   * A LineParser is an interface to parse a line in a text file. Separate
   * LineParsers and are used for OBJ and MTL files.
   */
  protected interface LineParser {
    public void parseLine(String line) throws IOException,
        NumberFormatException, NoSuchElementException;
  }

  protected File path;

  protected List vertices;

  protected Material currentMaterial;

  protected HashMap materials;

  protected List lights;

  protected float ambientLightIntensity;

  protected HashMap parsers;

  private PolygonGroup object;

  private PolygonGroup currentGroup;

  /**
   * Creates a new ObjectLoader.
   */
  public ObjectLoader() {
    materials = new HashMap();
    vertices = new ArrayList();
    parsers = new HashMap();
    parsers.put("obj", new ObjLineParser());
    parsers.put("mtl", new MtlLineParser());
    currentMaterial = null;
    setLights(new ArrayList(), 1);
  }

  /**
   * Sets the lights used for the polygons in the parsed objects. After
   * calling this method calls to loadObject use these lights.
   */
  public void setLights(List lights, float ambientLightIntensity) {
    this.lights = lights;
    this.ambientLightIntensity = ambientLightIntensity;
  }

  /**
   * Loads an OBJ file as a PolygonGroup.
   */
  public PolygonGroup loadObject(String filename) throws IOException {
    File file = new File(filename);
    object = new PolygonGroup();
    object.setFilename(file.getName());
    path = file.getParentFile();

    vertices.clear();
    currentGroup = object;
    parseFile(filename);

    return object;
  }

  /**
   * Gets a Vector3D from the list of vectors in the file. Negative indeces
   * count from the end of the list, postive indeces count from the beginning.
   * 1 is the first index, -1 is the last. 0 is invalid and throws an
   * exception.
   */
  protected Vector3D getVector(String indexStr) {
    int index = Integer.parseInt(indexStr);
    if (index < 0) {
      index = vertices.size() + index + 1;
    }
    return (Vector3D) vertices.get(index - 1);
  }

  /**
   * Parses an OBJ (ends with ".obj") or MTL file (ends with ".mtl").
   */
  protected void parseFile(String filename) throws IOException {
    // get the file relative to the source path
    File file = new File(path, filename);
    BufferedReader reader = new BufferedReader(new FileReader(file));

    // get the parser based on the file extention
    LineParser parser = null;
    int extIndex = filename.lastIndexOf('.');
    if (extIndex != -1) {
      String ext = filename.substring(extIndex + 1);
      parser = (LineParser) parsers.get(ext.toLowerCase());
    }
    if (parser == null) {
      parser = (LineParser) parsers.get("obj");
    }

    // parse every line in the file
    while (true) {
      String line = reader.readLine();
      // no more lines to read
      if (line == null) {
        reader.close();
        return;
      }

      line = line.trim();

      // ignore blank lines and comments
      if (line.length() > 0 && !line.startsWith("#")) {
        // interpret the line
        try {
          parser.parseLine(line);
        } catch (NumberFormatException ex) {
          throw new IOException(ex.getMessage());
        } catch (NoSuchElementException ex) {
          throw new IOException(ex.getMessage());
        }
      }

    }
  }

  /**
   * Parses a line in an OBJ file.
   */
  protected class ObjLineParser implements LineParser {

    public void parseLine(String line) throws IOException,
        NumberFormatException, NoSuchElementException {
      StringTokenizer tokenizer = new StringTokenizer(line);
      String command = tokenizer.nextToken();
      if (command.equals("v")) {
        // create a new vertex
        vertices.add(new Vector3D(Float.parseFloat(tokenizer
            .nextToken()), Float.parseFloat(tokenizer.nextToken()),
            Float.parseFloat(tokenizer.nextToken())));
      } else if (command.equals("f")) {
        // create a new face (flat, convex polygon)
        List currVertices = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
          String indexStr = tokenizer.nextToken();

          // ignore texture and normal coords
          int endIndex = indexStr.indexOf('/');
          if (endIndex != -1) {
            indexStr = indexStr.substring(0, endIndex);
          }

          currVertices.add(getVector(indexStr));
        }

        // create textured polygon
        Vector3D[] array = new Vector3D[currVertices.size()];
        currVertices.toArray(array);
        TexturedPolygon3D poly = new TexturedPolygon3D(array);

        // set the texture
        ShadedSurface.createShadedSurface(poly,
            currentMaterial.texture, lights, ambientLightIntensity);

        // add the polygon to the current group
        currentGroup.addPolygon(poly);
      } else if (command.equals("g")) {
        // define the current group
        if (tokenizer.hasMoreTokens()) {
          String name = tokenizer.nextToken();
          currentGroup = new PolygonGroup(name);
        } else {
          currentGroup = new PolygonGroup();
        }
        object.addPolygonGroup(currentGroup);
      } else if (command.equals("mtllib")) {
        // load materials from file
        String name = tokenizer.nextToken();
        parseFile(name);
      } else if (command.equals("usemtl")) {
        // define the current material
        String name = tokenizer.nextToken();
        currentMaterial = (Material) materials.get(name);
        if (currentMaterial == null) {
          System.out.println("no material: " + name);
        }
      } else {
        // unknown command - ignore it
      }

    }
  }

  /**
   * Parses a line in a material MTL file.
   */
  protected class MtlLineParser implements LineParser {

    public void parseLine(String line) throws NoSuchElementException {
      StringTokenizer tokenizer = new StringTokenizer(line);
      String command = tokenizer.nextToken();

      if (command.equals("newmtl")) {
        // create a new material if needed
        String name = tokenizer.nextToken();
        currentMaterial = (Material) materials.get(name);
        if (currentMaterial == null) {
          currentMaterial = new Material();
          materials.put(name, currentMaterial);
        }
      } else if (command.equals("map_Kd")) {
        // give the current material a texture
        String name = tokenizer.nextToken();
        File file = new File(path, name);
        if (!file.equals(currentMaterial.sourceFile)) {
          currentMaterial.sourceFile = file;
          currentMaterial.texture = (ShadedTexture) Texture
              .createTexture(file.getPath(), true);
        }
      } else {
        // unknown command - ignore it
      }
    }
  }
}

class BSPLine extends Line2D.Float {

  public static final int BACK = -1;

  public static final int COLLINEAR = 0;

  public static final int FRONT = 1;

  public static final int SPANNING = 2;

  /**
   * X coordinate of the line normal.
   */
  public float nx;

  /**
   * Y coordinate of the line normal.
   */
  public float ny;

  /**
   * Top-most location of a line representing a wall.
   */
  public float top;

  /**
   * Bottom-most location of a line representing a wall.
   */
  public float bottom;

  /**
   * Creates a new line from (0,0) to (0,0)
   */
  public BSPLine() {
    super();
  }

  /**
   * Creates a new BSPLine based on the specified BSPPolygon (only if the
   * BSPPolygon is a vertical wall).
   */
  public BSPLine(BSPPolygon poly) {
    setTo(poly);
  }

  /**
   * Creates a new BSPLine based on the specified coordinates.
   */
  public BSPLine(float x1, float y1, float x2, float y2) {
    setLine(x1, y1, x2, y2);
  }

  /**
   * Sets this BSPLine to the specified BSPPolygon (only if the BSPPolygon is
   * a vertical wall).
   */
  public void setTo(BSPPolygon poly) {
    if (!poly.isWall()) {
      throw new IllegalArgumentException("BSPPolygon not a wall");
    }
    top = java.lang.Float.MIN_VALUE;
    bottom = java.lang.Float.MAX_VALUE;
    // find the two points (ignoring y) that are farthest apart
    float distance = -1;
    for (int i = 0; i < poly.getNumVertices(); i++) {
      Vector3D v1 = poly.getVertex(i);
      top = Math.max(top, v1.y);
      bottom = Math.min(bottom, v1.y);
      for (int j = 0; j < poly.getNumVertices(); j++) {
        Vector3D v2 = poly.getVertex(j);
        float newDist = (float) Point2D.distanceSq(v1.x, v1.z, v2.x,
            v2.z);
        if (newDist > distance) {
          distance = newDist;
          x1 = v1.x;
          y1 = v1.z;
          x2 = v2.x;
          y2 = v2.z;
        }
      }
    }
    nx = poly.getNormal().x;
    ny = poly.getNormal().z;
  }

  /**
   * Calculates the normal to this line.
   */
  public void calcNormal() {
    nx = y2 - y1;
    ny = x1 - x2;
  }

  /**
   * Normalizes the normal of this line (make the normal's length 1).
   */
  public void normalize() {
    float length = (float) Math.sqrt(nx * nx + ny * ny);
    nx /= length;
    ny /= length;
  }

  public void setLine(float x1, float y1, float x2, float y2) {
    super.setLine(x1, y1, x2, y2);
    calcNormal();
  }

  public void setLine(double x1, double y1, double x2, double y2) {
    super.setLine(x1, y1, x2, y2);
    calcNormal();
  }

  /**
   * Flips this line so that the end points are reversed (in other words,
   * (x1,y1) becomes (x2,y2) and vice versa) and the normal is changed to
   * point the opposite direction.
   */
  public void flip() {
    float tx = x1;
    float ty = y1;
    x1 = x2;
    y1 = y2;
    x2 = tx;
    y2 = ty;
    nx = -nx;
    ny = -ny;
  }

  /**
   * Sets the top and bottom height of this "wall".
   */
  public void setHeight(float top, float bottom) {
    this.top = top;
    this.bottom = bottom;
  }

  /**
   * Returns true if the endpoints of this line match the endpoints of the
   * specified line. Ignores normal and height values.
   */
  public boolean equals(BSPLine line) {
    return (x1 == line.x1 && x2 == line.x2 && y1 == line.y1 && y2 == line.y2);
  }

  /**
   * Returns true if the endpoints of this line match the endpoints of the
   * specified line, ignoring endpoint order (if the first point of this line
   * is equal to the second point of the specified line, and vice versa,
   * returns true). Ignores normal and height values.
   */
  public boolean equalsIgnoreOrder(BSPLine line) {
    return equals(line)
        || ((x1 == line.x2 && x2 == line.x1 && y1 == line.y2 && y2 == line.y1));
  }

  public String toString() {
    return "(" + x1 + ", " + y1 + ")->" + "(" + x2 + "," + y2 + ")"
        + " bottom: " + bottom + " top: " + top;
  }

  /**
   * Gets the side of this line the specified point is on. This method treats
   * the line as 1-unit thick, so points within this 1-unit border are
   * considered collinear. For this to work correctly, the normal of this line
   * must be normalized, either by setting this line to a polygon or by
   * calling normalize(). Returns either FRONT, BACK, or COLLINEAR.
   */
  public int getSideThick(float x, float y) {
    int frontSide = getSideThin(x - nx / 2, y - ny / 2);
    if (frontSide == FRONT) {
      return FRONT;
    } else if (frontSide == BACK) {
      int backSide = getSideThin(x + nx / 2, y + ny / 2);
      if (backSide == BACK) {
        return BACK;
      }
    }
    return COLLINEAR;
  }

  /**
   * Gets the side of this line the specified point is on. Because of floating
   * point inaccuracy, a collinear line will be rare. For this to work
   * correctly, the normal of this line must be normalized, either by setting
   * this line to a polygon or by calling normalize(). Returns either FRONT,
   * BACK, or COLLINEAR.
   */
  public int getSideThin(float x, float y) {
    // dot product between vector to the point and the normal
    float side = (x - x1) * nx + (y - y1) * ny;
    return (side < 0) ? BACK : (side > 0) ? FRONT : COLLINEAR;
  }

  /**
   * Gets the side of this line that the specified line segment is on. Returns
   * either FRONT, BACK, COLINEAR, or SPANNING.
   */
  public int getSide(Line2D.Float segment) {
    if (this == segment) {
      return COLLINEAR;
    }
    int p1Side = getSideThick(segment.x1, segment.y1);
    int p2Side = getSideThick(segment.x2, segment.y2);
    if (p1Side == p2Side) {
      return p1Side;
    } else if (p1Side == COLLINEAR) {
      return p2Side;
    } else if (p2Side == COLLINEAR) {
      return p1Side;
    } else {
      return SPANNING;
    }
  }

  /**
   * Gets the side of this line that the specified polygon is on. Returns
   * either FRONT, BACK, COLINEAR, or SPANNING.
   */
  public int getSide(BSPPolygon poly) {
    boolean onFront = false;
    boolean onBack = false;

    // check every point
    for (int i = 0; i < poly.getNumVertices(); i++) {
      Vector3D v = poly.getVertex(i);
      int side = getSideThick(v.x, v.z);
      if (side == BSPLine.FRONT) {
        onFront = true;
      } else if (side == BSPLine.BACK) {
        onBack = true;
      }
    }

    // classify the polygon
    if (onFront && onBack) {
      return BSPLine.SPANNING;
    } else if (onFront) {
      return BSPLine.FRONT;
    } else if (onBack) {
      return BSPLine.BACK;
    } else {
      return BSPLine.COLLINEAR;
    }
  }

  /**
   * Returns the fraction of intersection along this line. Returns a value
   * from 0 to 1 if the segments intersect. For example, a return value of 0
   * means the intersection occurs at point (x1, y1), 1 means the intersection
   * occurs at point (x2, y2), and .5 mean the intersection occurs halfway
   * between the two endpoints of this line. Returns -1 if the lines are
   * parallel.
   */
  public float getIntersection(Line2D.Float line) {
    // The intersection point I, of two vectors, A1->A2 and
    // B1->B2, is:
    // I = A1 + Ua * (A2 - A1)
    // I = B1 + Ub * (B2 - B1)
    //
    // Solving for Ua gives us the following formula.
    // Ua is returned.
    float denominator = (line.y2 - line.y1) * (x2 - x1)
        - (line.x2 - line.x1) * (y2 - y1);

    // check if the two lines are parallel
    if (denominator == 0) {
      return -1;
    }

    float numerator = (line.x2 - line.x1) * (y1 - line.y1)
        - (line.y2 - line.y1) * (x1 - line.x1);

    return numerator / denominator;
  }

  /**
   * Returns the interection point of this line with the specified line.
   */
  public Point2D.Float getIntersectionPoint(Line2D.Float line) {
    return getIntersectionPoint(line, null);
  }

  /**
   * Returns the interection of this line with the specified line. If
   * interesection is null, a new point is created.
   */
  public Point2D.Float getIntersectionPoint(Line2D.Float line,
      Point2D.Float intersection) {
    if (intersection == null) {
      intersection = new Point2D.Float();
    }
    float fraction = getIntersection(line);
    intersection.setLocation(x1 + fraction * (x2 - x1), y1 + fraction
        * (y2 - y1));
    return intersection;
  }

}
/**
 * The BSPTree class represents a 2D Binary Space Partitioned tree of polygons.
 * The BSPTree is built using a BSPTreeBuilder class, and can be travered using
 * BSPTreeTraverser class.
 */

class BSPTree {

  /**
   * A Node of the tree. All children of the node are either to the front of
   * back of the node's partition.
   */
  public static class Node {
    public Node front;

    public Node back;

    public BSPLine partition;

    public List polygons;
  }

  /**
   * A Leaf of the tree. A leaf has no partition or front or back nodes.
   */
  public static class Leaf extends Node {
    public float floorHeight;

    public float ceilHeight;

    public boolean isBack;

    public List portals;

    public Rectangle bounds;
  }

  private Node root;

  /**
   * Creates a new BSPTree with the specified root node.
   */
  public BSPTree(Node root) {
    this.root = root;
  }

  /**
   * Gets the root node of this tree.
   */
  public Node getRoot() {
    return root;
  }

  /**
   * Calculates the 2D boundary of all the polygons in this BSP tree. Returns
   * a rectangle of the bounds.
   */
  public Rectangle calcBounds() {

    final Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    final Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

    BSPTreeTraverser traverser = new BSPTreeTraverser();
    traverser.setListener(new BSPTreeTraverseListener() {

      public boolean visitPolygon(BSPPolygon poly, boolean isBack) {
        for (int i = 0; i < poly.getNumVertices(); i++) {
          Vector3D v = poly.getVertex(i);
          int x = (int) Math.floor(v.x);
          int y = (int) Math.floor(v.z);
          min.x = Math.min(min.x, x);
          max.x = Math.max(max.x, x);
          min.y = Math.min(min.y, y);
          max.y = Math.max(max.y, y);
        }

        return true;
      }
    });

    traverser.traverse(this);

    return new Rectangle(min.x, min.y, max.x - min.x, max.y - min.y);
  }

  /**
   * Gets the leaf the x,z coordinates are in.
   */
  public Leaf getLeaf(float x, float z) {
    return getLeaf(root, x, z);
  }

  protected Leaf getLeaf(Node node, float x, float z) {
    if (node == null || node instanceof Leaf) {
      return (Leaf) node;
    }
    int side = node.partition.getSideThin(x, z);
    if (side == BSPLine.BACK) {
      return getLeaf(node.back, x, z);
    } else {
      return getLeaf(node.front, x, z);
    }
  }

  /**
   * Gets the Node that is collinear with the specified partition, or null if
   * no such node exists.
   */
  public Node getCollinearNode(BSPLine partition) {
    return getCollinearNode(root, partition);
  }

  protected Node getCollinearNode(Node node, BSPLine partition) {
    if (node == null || node instanceof Leaf) {
      return null;
    }
    int side = node.partition.getSide(partition);
    if (side == BSPLine.COLLINEAR) {
      return node;
    }
    if (side == BSPLine.FRONT) {
      return getCollinearNode(node.front, partition);
    } else if (side == BSPLine.BACK) {
      return getCollinearNode(node.back, partition);
    } else {
      // BSPLine.SPANNING: first try front, then back
      Node front = getCollinearNode(node.front, partition);
      if (front != null) {
        return front;
      } else {
        return getCollinearNode(node.back, partition);
      }
    }
  }

  /**
   * Gets the Leaf in front of the specified partition.
   */
  public Leaf getFrontLeaf(BSPLine partition) {
    return getLeaf(root, partition, BSPLine.FRONT);
  }

  /**
   * Gets the Leaf in back of the specified partition.
   */
  public Leaf getBackLeaf(BSPLine partition) {
    return getLeaf(root, partition, BSPLine.BACK);
  }

  protected Leaf getLeaf(Node node, BSPLine partition, int side) {
    if (node == null || node instanceof Leaf) {
      return (Leaf) node;
    }
    int segSide = node.partition.getSide(partition);
    if (segSide == BSPLine.COLLINEAR) {
      segSide = side;
    }
    if (segSide == BSPLine.FRONT) {
      return getLeaf(node.front, partition, side);
    } else if (segSide == BSPLine.BACK) {
      return getLeaf(node.back, partition, side);
    } else { // BSPLine.SPANNING
      // shouldn't happen
      return null;
    }
  }

  /**
   * Creates surface textures for every polygon in this tree.
   */
  public void createSurfaces(final List lights) {
    BSPTreeTraverser traverser = new BSPTreeTraverser();
    traverser.setListener(new BSPTreeTraverseListener() {

      public boolean visitPolygon(BSPPolygon poly, boolean isBack) {
        Texture texture = poly.getTexture();
        if (texture instanceof ShadedTexture) {
          ShadedSurface.createShadedSurface(poly,
              (ShadedTexture) texture, poly.getTextureBounds(),
              lights, poly.getAmbientLightIntensity());
        }
        return true;
      }
    });

    traverser.traverse(this);
  }

}

/**
 * A ShadedSurface is a pre-shaded Texture that maps onto a polygon.
 */

final class ShadedSurface extends Texture {

  public static final int SURFACE_BORDER_SIZE = 1;

  public static final int SHADE_RES_BITS = 4;

  public static final int SHADE_RES = 1 << SHADE_RES_BITS;

  public static final int SHADE_RES_MASK = SHADE_RES - 1;

  public static final int SHADE_RES_SQ = SHADE_RES * SHADE_RES;

  public static final int SHADE_RES_SQ_BITS = SHADE_RES_BITS * 2;

  private short[] buffer;

  private SoftReference bufferReference;

  private boolean dirty;

  private ShadedTexture sourceTexture;

  private Rectangle3D sourceTextureBounds;

  private Rectangle3D surfaceBounds;

  private byte[] shadeMap;

  private int shadeMapWidth;

  private int shadeMapHeight;

  // for incrementally calculating shade values
  private int shadeValue;

  private int shadeValueInc;

  /**
   * Creates a ShadedSurface with the specified width and height.
   */
  public ShadedSurface(int width, int height) {
    this(null, width, height);
  }

  /**
   * Creates a ShadedSurface with the specified buffer, width and height.
   */
  public ShadedSurface(short[] buffer, int width, int height) {
    super(width, height);
    this.buffer = buffer;
    bufferReference = new SoftReference(buffer);
    sourceTextureBounds = new Rectangle3D();
    dirty = true;
  }

  /**
   * Creates a ShadedSurface for the specified polygon. The shade map is
   * created from the specified list of point lights and ambient light
   * intensity.
   */
  public static void createShadedSurface(TexturedPolygon3D poly,
      ShadedTexture texture, List lights, float ambientLightIntensity) {
    // create the texture bounds
    Vector3D origin = poly.getVertex(0);
    Vector3D dv = new Vector3D(poly.getVertex(1));
    dv.subtract(origin);
    Vector3D du = new Vector3D();
    du.setToCrossProduct(poly.getNormal(), dv);
    Rectangle3D bounds = new Rectangle3D(origin, du, dv,
        texture.getWidth(), texture.getHeight());

    createShadedSurface(poly, texture, bounds, lights,
        ambientLightIntensity);
  }

  /**
   * Creates a ShadedSurface for the specified polygon. The shade map is
   * created from the specified list of point lights and ambient light
   * intensity.
   */
  public static void createShadedSurface(TexturedPolygon3D poly,
      ShadedTexture texture, Rectangle3D textureBounds, List lights,
      float ambientLightIntensity) {

    // create the surface bounds
    poly.setTexture(texture, textureBounds);
    Rectangle3D surfaceBounds = poly.calcBoundingRectangle();

    // give the surfaceBounds a border to correct for
    // slight errors when texture mapping
    Vector3D du = new Vector3D(surfaceBounds.getDirectionU());
    Vector3D dv = new Vector3D(surfaceBounds.getDirectionV());
    du.multiply(SURFACE_BORDER_SIZE);
    dv.multiply(SURFACE_BORDER_SIZE);
    surfaceBounds.getOrigin().subtract(du);
    surfaceBounds.getOrigin().subtract(dv);
    int width = (int) Math.ceil(surfaceBounds.getWidth()
        + SURFACE_BORDER_SIZE * 2);
    int height = (int) Math.ceil(surfaceBounds.getHeight()
        + SURFACE_BORDER_SIZE * 2);
    surfaceBounds.setWidth(width);
    surfaceBounds.setHeight(height);

    // create the shaded surface texture
    ShadedSurface surface = new ShadedSurface(width, height);
    surface.setTexture(texture, textureBounds);
    surface.setSurfaceBounds(surfaceBounds);

    // create the surface's shade map
    surface.buildShadeMap(lights, ambientLightIntensity);

    // set the polygon's surface
    poly.setTexture(surface, surfaceBounds);
  }

  /**
   * Gets the 16-bit color of the pixel at location (x,y) in the bitmap. The x
   * and y values are assumbed to be within the bounds of the surface;
   * otherwise an ArrayIndexOutOfBoundsException occurs.
   */
  public short getColor(int x, int y) {
    //try {
    return buffer[x + y * width];
    //}
    //catch (ArrayIndexOutOfBoundsException ex) {
    //    return -2048;
    //}

  }

  /**
   * Gets the 16-bit color of the pixel at location (x,y) in the bitmap. The x
   * and y values are checked to be within the bounds of the surface, and if
   * not, the pixel on the edge of the texture is returned.
   */
  public short getColorChecked(int x, int y) {
    if (x < 0) {
      x = 0;
    } else if (x >= width) {
      x = width - 1;
    }
    if (y < 0) {
      y = 0;
    } else if (y >= height) {
      y = height - 1;
    }
    return getColor(x, y);
  }

  /**
   * Marks whether this surface is dirty. Surfaces marked as dirty may be
   * cleared externally.
   */
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  /**
   * Checks wether this surface is dirty. Surfaces marked as dirty may be
   * cleared externally.
   */
  public boolean isDirty() {
    return dirty;
  }

  /**
   * Creates a new surface and add a SoftReference to it.
   */
  protected void newSurface(int width, int height) {
    buffer = new short[width * height];
    bufferReference = new SoftReference(buffer);
  }

  /**
   * Clears this surface, allowing the garbage collector to remove it from
   * memory if needed.
   */
  public void clearSurface() {
    buffer = null;
  }

  /**
   * Checks if the surface has been cleared.
   */
  public boolean isCleared() {
    return (buffer == null);
  }

  /**
   * If the buffer has been previously built and cleared but not yet removed
   * from memory by the garbage collector, then this method attempts to
   * retrieve it. Returns true if successfull.
   */
  public boolean retrieveSurface() {
    if (buffer == null) {
      buffer = (short[]) bufferReference.get();
    }
    return !(buffer == null);
  }

  /**
   * Sets the source texture for this ShadedSurface.
   */
  public void setTexture(ShadedTexture texture) {
    this.sourceTexture = texture;
    sourceTextureBounds.setWidth(texture.getWidth());
    sourceTextureBounds.setHeight(texture.getHeight());
  }

  /**
   * Sets the source texture and source bounds for this ShadedSurface.
   */
  public void setTexture(ShadedTexture texture, Rectangle3D bounds) {
    setTexture(texture);
    sourceTextureBounds.setTo(bounds);
  }

  /**
   * Sets the surface bounds for this ShadedSurface.
   */
  public void setSurfaceBounds(Rectangle3D surfaceBounds) {
    this.surfaceBounds = surfaceBounds;
  }

  /**
   * Gets the surface bounds for this ShadedSurface.
   */
  public Rectangle3D getSurfaceBounds() {
    return surfaceBounds;
  }

  /**
   * Builds the surface. First, this method calls retrieveSurface() to see if
   * the surface needs to be rebuilt. If not, the surface is built by tiling
   * the source texture and apply the shade map.
   */
  public void buildSurface() {

    if (retrieveSurface()) {
      return;
    }

    int width = (int) surfaceBounds.getWidth();
    int height = (int) surfaceBounds.getHeight();

    // create a new surface (buffer)
    newSurface(width, height);

    // builds the surface.
    // assume surface bounds and texture bounds are aligned
    // (possibly with different origins)
    Vector3D origin = sourceTextureBounds.getOrigin();
    Vector3D directionU = sourceTextureBounds.getDirectionU();
    Vector3D directionV = sourceTextureBounds.getDirectionV();

    Vector3D d = new Vector3D(surfaceBounds.getOrigin());
    d.subtract(origin);
    int startU = (int) ((d.getDotProduct(directionU) - SURFACE_BORDER_SIZE));
    int startV = (int) ((d.getDotProduct(directionV) - SURFACE_BORDER_SIZE));
    int offset = 0;
    int shadeMapOffsetU = SHADE_RES - SURFACE_BORDER_SIZE - startU;
    int shadeMapOffsetV = SHADE_RES - SURFACE_BORDER_SIZE - startV;

    for (int v = startV; v < startV + height; v++) {
      sourceTexture.setCurrRow(v);
      int u = startU;
      int amount = SURFACE_BORDER_SIZE;
      while (u < startU + width) {
        getInterpolatedShade(u + shadeMapOffsetU, v + shadeMapOffsetV);

        // keep drawing until we need to recalculate
        // the interpolated shade. (every SHADE_RES pixels)
        int endU = Math.min(startU + width, u + amount);
        while (u < endU) {
          buffer[offset++] = sourceTexture.getColorCurrRow(u,
              shadeValue >> SHADE_RES_SQ_BITS);
          shadeValue += shadeValueInc;
          u++;
        }
        amount = SHADE_RES;
      }
    }

    // if the surface bounds is not aligned with the texture
    // bounds, use this (slower) code.
    /*
     * Vector3D origin = sourceTextureBounds.getOrigin(); Vector3D
     * directionU = sourceTextureBounds.getDirectionU(); Vector3D directionV =
     * sourceTextureBounds.getDirectionV();
     * 
     * Vector3D d = new Vector3D(surfaceBounds.getOrigin());
     * d.subtract(origin); int initTextureU = (int)(SCALE *
     * (d.getDotProduct(directionU) - SURFACE_BORDER_SIZE)); int
     * initTextureV = (int)(SCALE * (d.getDotProduct(directionV) -
     * SURFACE_BORDER_SIZE)); int textureDu1 = (int)(SCALE *
     * directionU.getDotProduct( surfaceBounds.getDirectionV())); int
     * textureDv1 = (int)(SCALE * directionV.getDotProduct(
     * surfaceBounds.getDirectionV())); int textureDu2 = (int)(SCALE *
     * directionU.getDotProduct( surfaceBounds.getDirectionU())); int
     * textureDv2 = (int)(SCALE * directionV.getDotProduct(
     * surfaceBounds.getDirectionU()));
     * 
     * int shadeMapOffset = SHADE_RES - SURFACE_BORDER_SIZE;
     * 
     * for (int v=0; v <height; v++) { int textureU = initTextureU; int
     * textureV = initTextureV;
     * 
     * for (int u=0; u <width; u++) { if (((u + shadeMapOffset) &
     * SHADE_RES_MASK) == 0) { getInterpolatedShade(u + shadeMapOffset, v +
     * shadeMapOffset); } buffer[offset++] = sourceTexture.getColor(
     * textureU >> SCALE_BITS, textureV >> SCALE_BITS, shadeValue >>
     * SHADE_RES_SQ_BITS); textureU+=textureDu2; textureV+=textureDv2;
     * shadeValue+=shadeValueInc; } initTextureU+=textureDu1;
     * initTextureV+=textureDv1; }
     */
  }

  /**
   * Gets the shade (from the shade map) for the specified (u,v) location. The
   * u and v values should be left-shifted by SHADE_RES_BITS, and the extra
   * bits are used to interpolate between values. For an interpolation
   * example, a location halfway between shade values 1 and 3 would return 2.
   */
  public int getInterpolatedShade(int u, int v) {

    int fracU = u & SHADE_RES_MASK;
    int fracV = v & SHADE_RES_MASK;

    int offset = (u >> SHADE_RES_BITS)
        + ((v >> SHADE_RES_BITS) * shadeMapWidth);

    int shade00 = (SHADE_RES - fracV) * shadeMap[offset];
    int shade01 = fracV * shadeMap[offset + shadeMapWidth];
    int shade10 = (SHADE_RES - fracV) * shadeMap[offset + 1];
    int shade11 = fracV * shadeMap[offset + shadeMapWidth + 1];

    shadeValue = SHADE_RES_SQ / 2 + (SHADE_RES - fracU) * shade00
        + (SHADE_RES - fracU) * shade01 + fracU * shade10 + fracU
        * shade11;

    // the value to increment as u increments
    shadeValueInc = -shade00 - shade01 + shade10 + shade11;

    return shadeValue >> SHADE_RES_SQ_BITS;
  }

  /**
   * Gets the shade (from the built shade map) for the specified (u,v)
   * location.
   */
  public int getShade(int u, int v) {
    return shadeMap[u + v * shadeMapWidth];
  }

  /**
   * Builds the shade map for this surface from the specified list of point
   * lights and the ambiant light intensity.
   */
  public void buildShadeMap(List pointLights, float ambientLightIntensity) {

    Vector3D surfaceNormal = surfaceBounds.getNormal();

    int polyWidth = (int) surfaceBounds.getWidth() - SURFACE_BORDER_SIZE
        * 2;
    int polyHeight = (int) surfaceBounds.getHeight() - SURFACE_BORDER_SIZE
        * 2;
    // assume SURFACE_BORDER_SIZE is <= SHADE_RES
    shadeMapWidth = polyWidth / SHADE_RES + 4;
    shadeMapHeight = polyHeight / SHADE_RES + 4;
    shadeMap = new byte[shadeMapWidth * shadeMapHeight];

    // calculate the shade map origin
    Vector3D origin = new Vector3D(surfaceBounds.getOrigin());
    Vector3D du = new Vector3D(surfaceBounds.getDirectionU());
    Vector3D dv = new Vector3D(surfaceBounds.getDirectionV());
    du.multiply(SHADE_RES - SURFACE_BORDER_SIZE);
    dv.multiply(SHADE_RES - SURFACE_BORDER_SIZE);
    origin.subtract(du);
    origin.subtract(dv);

    // calculate the shade for each sample point.
    Vector3D point = new Vector3D();
    du.setTo(surfaceBounds.getDirectionU());
    dv.setTo(surfaceBounds.getDirectionV());
    du.multiply(SHADE_RES);
    dv.multiply(SHADE_RES);
    for (int v = 0; v < shadeMapHeight; v++) {
      point.setTo(origin);
      for (int u = 0; u < shadeMapWidth; u++) {
        shadeMap[u + v * shadeMapWidth] = calcShade(surfaceNormal,
            point, pointLights, ambientLightIntensity);
        point.add(du);
      }
      origin.add(dv);
    }
  }

  /**
   * Determine the shade of a point on the polygon. This computes the
   * Lambertian reflection for a point on the plane. Each point light has an
   * intensity and a distance falloff value, but no specular reflection or
   * shadows from other polygons are computed. The value returned is from 0 to
   * ShadedTexture.MAX_LEVEL.
   */
  protected byte calcShade(Vector3D normal, Vector3D point, List pointLights,
      float ambientLightIntensity) {
    float intensity = 0;
    Vector3D directionToLight = new Vector3D();

    for (int i = 0; i < pointLights.size(); i++) {
      PointLight3D light = (PointLight3D) pointLights.get(i);
      directionToLight.setTo(light);
      directionToLight.subtract(point);

      float distance = directionToLight.length();
      directionToLight.normalize();
      float lightIntensity = light.getIntensity(distance)
          * directionToLight.getDotProduct(normal);
      lightIntensity = Math.min(lightIntensity, 1);
      lightIntensity = Math.max(lightIntensity, 0);
      intensity += lightIntensity;
    }

    intensity = Math.min(intensity, 1);
    intensity = Math.max(intensity, 0);

    intensity += ambientLightIntensity;

    intensity = Math.min(intensity, 1);
    intensity = Math.max(intensity, 0);
    int level = Math.round(intensity * ShadedTexture.MAX_LEVEL);
    return (byte) level;
  }
}

/**
 * A BSPTreeTraverer traverses a 2D BSP tree either with a in-order or
 * draw-order (front-to-back) order. Visited polygons are signaled using a
 * BSPTreeTraverseListener.
 */

class BSPTreeTraverser {

  private boolean traversing;

  private float x;

  private float z;

  private GameObjectManager objectManager;

  private BSPTreeTraverseListener listener;

  /**
   * Creates a new BSPTreeTraverser with no BSPTreeTraverseListener.
   */
  public BSPTreeTraverser() {
    this(null);
  }

  /**
   * Creates a new BSPTreeTraverser with the specified
   * BSPTreeTraverseListener.
   */
  public BSPTreeTraverser(BSPTreeTraverseListener listener) {
    setListener(listener);
  }

  /**
   * Sets the BSPTreeTraverseListener to use during traversals.
   */
  public void setListener(BSPTreeTraverseListener listener) {
    this.listener = listener;
  }

  /**
   * Sets the GameObjectManager. If the GameObjectManager is not null during
   * traversal, then the manager's markVisible() method is called to specify
   * visible parts of the tree.
   */
  public void setGameObjectManager(GameObjectManager objectManager) {
    this.objectManager = objectManager;
  }

  /**
   * Traverses a tree in draw-order (front-to-back) using the specified view
   * location.
   */
  public void traverse(BSPTree tree, Vector3D viewLocation) {
    x = viewLocation.x;
    z = viewLocation.z;
    traversing = true;
    traverseDrawOrder(tree.getRoot());
  }

  /**
   * Traverses a tree in in-order.
   */
  public void traverse(BSPTree tree) {
    traversing = true;
    traverseInOrder(tree.getRoot());
  }

  /**
   * Traverses a node in draw-order (front-to-back) using the current view
   * location.
   */
  private void traverseDrawOrder(BSPTree.Node node) {
    if (traversing && node != null) {
      if (node instanceof BSPTree.Leaf) {
        // no partition, just handle polygons
        visitNode(node);
      } else if (node.partition.getSideThin(x, z) != BSPLine.BACK) {
        traverseDrawOrder(node.front);
        visitNode(node);
        traverseDrawOrder(node.back);
      } else {
        traverseDrawOrder(node.back);
        visitNode(node);
        traverseDrawOrder(node.front);
      }
    }

  }

  /**
   * Traverses a node in in-order.
   */
  private void traverseInOrder(BSPTree.Node node) {
    if (traversing && node != null) {
      traverseInOrder(node.front);
      visitNode(node);
      traverseInOrder(node.back);
    }
  }

  /**
   * Visits a node in the tree. The BSPTreeTraverseListener's visitPolygon()
   * method is called for every polygon in the node.
   */
  private void visitNode(BSPTree.Node node) {
    if (!traversing || node.polygons == null) {
      return;
    }

    boolean isBack = false;
    if (node instanceof BSPTree.Leaf) {
      BSPTree.Leaf leaf = (BSPTree.Leaf) node;
      isBack = leaf.isBack;
      // mark the bounds of this leaf as visible in
      // the game object manager.
      if (objectManager != null && leaf.bounds != null) {
        objectManager.markVisible(leaf.bounds);
      }
    }

    // visit every polygon
    for (int i = 0; traversing && i < node.polygons.size(); i++) {
      BSPPolygon poly = (BSPPolygon) node.polygons.get(i);
      traversing = listener.visitPolygon(poly, isBack);
    }
  }

}

/**
 * The SolidPolygonRenderer class transforms and draws solid-colored polygons
 * onto the screen.
 */

class SolidPolygonRenderer extends PolygonRenderer {

  public SolidPolygonRenderer(Transform3D camera, ViewWindow viewWindow) {
    this(camera, viewWindow, true);
  }

  public SolidPolygonRenderer(Transform3D camera, ViewWindow viewWindow,
      boolean clearViewEveryFrame) {
    super(camera, viewWindow, clearViewEveryFrame);
  }

  /**
   * Draws the current polygon. At this point, the current polygon is
   * transformed, clipped, projected, scan-converted, and visible.
   */
  protected void drawCurrentPolygon(Graphics2D g) {

    // set the color
    if (sourcePolygon instanceof SolidPolygon3D) {
      g.setColor(((SolidPolygon3D) sourcePolygon).getColor());
    } else {
      g.setColor(Color.GREEN);
    }

    // draw the scans
    int y = scanConverter.getTopBoundary();
    while (y <= scanConverter.getBottomBoundary()) {
      ScanConverter.Scan scan = scanConverter.getScan(y);
      if (scan.isValid()) {
        g.drawLine(scan.left, y, scan.right, y);
      }
      y++;
    }
  }
}
/**
 * The SolidPolygon3D class is a Polygon with a color.
 */

class SolidPolygon3D extends Polygon3D {

  private Color color = Color.GREEN;

  public SolidPolygon3D() {
    super();
  }

  public SolidPolygon3D(Vector3D v0, Vector3D v1, Vector3D v2) {
    this(new Vector3D[] { v0, v1, v2 });
  }

  public SolidPolygon3D(Vector3D v0, Vector3D v1, Vector3D v2, Vector3D v3) {
    this(new Vector3D[] { v0, v1, v2, v3 });
  }

  public SolidPolygon3D(Vector3D[] vertices) {
    super(vertices);
  }

  public void setTo(Polygon3D polygon) {
    super.setTo(polygon);
    if (polygon instanceof SolidPolygon3D) {
      color = ((SolidPolygon3D) polygon).color;
    }
  }

  /**
   * Gets the color of this solid-colored polygon used for rendering this
   * polygon.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of this solid-colored polygon used for rendering this
   * polygon.
   */
  public void setColor(Color color) {
    this.color = color;
  }

}

abstract class GameCore3D extends GameCore {

  private static final long INSTRUCTIONS_TIME = 4000;

  protected PolygonRenderer polygonRenderer;

  protected ViewWindow viewWindow;

  protected List polygons;

  private boolean drawFrameRate = false;

  private boolean drawInstructions = true;

  private long drawInstructionsTime = 0;

  // for calculating frame rate
  private int numFrames;

  private long startTime;

  private float frameRate;

  protected InputManager inputManager;

  private GameAction exit = new GameAction("exit");

  private GameAction smallerView = new GameAction("smallerView",
      GameAction.DETECT_INITAL_PRESS_ONLY);

  private GameAction largerView = new GameAction("largerView",
      GameAction.DETECT_INITAL_PRESS_ONLY);

  private GameAction frameRateToggle = new GameAction("frameRateToggle",
      GameAction.DETECT_INITAL_PRESS_ONLY);

  protected GameAction goForward = new GameAction("goForward");

  protected GameAction goBackward = new GameAction("goBackward");

  protected GameAction goUp = new GameAction("goUp");

  protected GameAction goDown = new GameAction("goDown");

  protected GameAction goLeft = new GameAction("goLeft");

  protected GameAction goRight = new GameAction("goRight");

  protected GameAction turnLeft = new GameAction("turnLeft");

  protected GameAction turnRight = new GameAction("turnRight");

  protected GameAction tiltUp = new GameAction("tiltUp");

  protected GameAction tiltDown = new GameAction("tiltDown");

  protected GameAction tiltLeft = new GameAction("tiltLeft");

  protected GameAction tiltRight = new GameAction("tiltRight");

  public void init(DisplayMode[] modes) {
    super.init(modes);

    inputManager = new InputManager(screen.getFullScreenWindow());
    inputManager.setRelativeMouseMode(true);
    inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

    inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    inputManager.mapToKey(goForward, KeyEvent.VK_W);
    inputManager.mapToKey(goForward, KeyEvent.VK_UP);
    inputManager.mapToKey(goBackward, KeyEvent.VK_S);
    inputManager.mapToKey(goBackward, KeyEvent.VK_DOWN);
    inputManager.mapToKey(goLeft, KeyEvent.VK_A);
    inputManager.mapToKey(goLeft, KeyEvent.VK_LEFT);
    inputManager.mapToKey(goRight, KeyEvent.VK_D);
    inputManager.mapToKey(goRight, KeyEvent.VK_RIGHT);
    inputManager.mapToKey(goUp, KeyEvent.VK_PAGE_UP);
    inputManager.mapToKey(goDown, KeyEvent.VK_PAGE_DOWN);
    inputManager.mapToMouse(turnLeft, InputManager.MOUSE_MOVE_LEFT);
    inputManager.mapToMouse(turnRight, InputManager.MOUSE_MOVE_RIGHT);
    inputManager.mapToMouse(tiltUp, InputManager.MOUSE_MOVE_UP);
    inputManager.mapToMouse(tiltDown, InputManager.MOUSE_MOVE_DOWN);

    inputManager.mapToKey(tiltLeft, KeyEvent.VK_INSERT);
    inputManager.mapToKey(tiltRight, KeyEvent.VK_DELETE);

    inputManager.mapToKey(smallerView, KeyEvent.VK_SUBTRACT);
    inputManager.mapToKey(smallerView, KeyEvent.VK_MINUS);
    inputManager.mapToKey(largerView, KeyEvent.VK_ADD);
    inputManager.mapToKey(largerView, KeyEvent.VK_PLUS);
    inputManager.mapToKey(largerView, KeyEvent.VK_EQUALS);
    inputManager.mapToKey(frameRateToggle, KeyEvent.VK_R);

    // create the polygon renderer
    createPolygonRenderer();

    // create polygons
    polygons = new ArrayList();
    createPolygons();
  }

  public abstract void createPolygons();

  public void createPolygonRenderer() {
    // make the view window the entire screen
    viewWindow = new ViewWindow(0, 0, screen.getWidth(),
        screen.getHeight(), (float) Math.toRadians(75));

    Transform3D camera = new Transform3D(0, 100, 0);
    polygonRenderer = new SolidPolygonRenderer(camera, viewWindow);
  }

  /**
   * Sets the view bounds, centering the view on the screen.
   */
  public void setViewBounds(int width, int height) {
    width = Math.min(width, screen.getWidth());
    height = Math.min(height, screen.getHeight());
    width = Math.max(64, width);
    height = Math.max(48, height);
    viewWindow.setBounds((screen.getWidth() - width) / 2, (screen
        .getHeight() - height) / 2, width, height);

    // clear the screen if view size changed
    // (clear both buffers)
    for (int i = 0; i < 2; i++) {
      Graphics2D g = screen.getGraphics();
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
      screen.update();
    }

  }

  public void update(long elapsedTime) {

    // check options
    if (exit.isPressed()) {
      stop();
      return;
    }
    if (largerView.isPressed()) {
      setViewBounds(viewWindow.getWidth() + 64,
          viewWindow.getHeight() + 48);
    } else if (smallerView.isPressed()) {
      setViewBounds(viewWindow.getWidth() - 64,
          viewWindow.getHeight() - 48);
    }
    if (frameRateToggle.isPressed()) {
      drawFrameRate = !drawFrameRate;
    }

    drawInstructionsTime += elapsedTime;
    if (drawInstructionsTime >= INSTRUCTIONS_TIME) {
      drawInstructions = false;
    }
    updateWorld(elapsedTime);
  }

  public void updateWorld(long elapsedTime) {

    // cap elapsedTime
    elapsedTime = Math.min(elapsedTime, 100);

    float angleChange = 0.0002f * elapsedTime;
    float distanceChange = .5f * elapsedTime;

    Transform3D camera = polygonRenderer.getCamera();
    Vector3D cameraLoc = camera.getLocation();

    // apply movement
    if (goForward.isPressed()) {
      cameraLoc.x -= distanceChange * camera.getSinAngleY();
      cameraLoc.z -= distanceChange * camera.getCosAngleY();
    }
    if (goBackward.isPressed()) {
      cameraLoc.x += distanceChange * camera.getSinAngleY();
      cameraLoc.z += distanceChange * camera.getCosAngleY();
    }
    if (goLeft.isPressed()) {
      cameraLoc.x -= distanceChange * camera.getCosAngleY();
      cameraLoc.z += distanceChange * camera.getSinAngleY();
    }
    if (goRight.isPressed()) {
      cameraLoc.x += distanceChange * camera.getCosAngleY();
      cameraLoc.z -= distanceChange * camera.getSinAngleY();
    }
    if (goUp.isPressed()) {
      cameraLoc.y += distanceChange;
    }
    if (goDown.isPressed()) {
      cameraLoc.y -= distanceChange;
    }

    // look up/down (rotate around x)
    int tilt = tiltUp.getAmount() - tiltDown.getAmount();
    tilt = Math.min(tilt, 200);
    tilt = Math.max(tilt, -200);

    // limit how far you can look up/down
    float newAngleX = camera.getAngleX() + tilt * angleChange;
    newAngleX = Math.max(newAngleX, (float) -Math.PI / 2);
    newAngleX = Math.min(newAngleX, (float) Math.PI / 2);
    camera.setAngleX(newAngleX);

    // turn (rotate around y)
    int turn = turnLeft.getAmount() - turnRight.getAmount();
    turn = Math.min(turn, 200);
    turn = Math.max(turn, -200);
    camera.rotateAngleY(turn * angleChange);

    // tilet head left/right (rotate around z)
    if (tiltLeft.isPressed()) {
      camera.rotateAngleZ(10 * angleChange);
    }
    if (tiltRight.isPressed()) {
      camera.rotateAngleZ(-10 * angleChange);
    }
  }

  public void draw(Graphics2D g) {
    int viewX1 = viewWindow.getLeftOffset();
    int viewY1 = viewWindow.getTopOffset();
    int viewX2 = viewX1 + viewWindow.getWidth();
    int viewY2 = viewY1 + viewWindow.getHeight();
    if (viewX1 != 0 || viewY1 != 0) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, viewX1, screen.getHeight());
      g.fillRect(viewX2, 0, screen.getWidth() - viewX2, screen
          .getHeight());
      g.fillRect(viewX1, 0, viewWindow.getWidth(), viewY1);
      g.fillRect(viewX1, viewY2, viewWindow.getWidth(), screen
          .getHeight()
          - viewY2);
    }

    drawPolygons(g);
    drawText(g);
  }

  public void drawPolygons(Graphics2D g) {
    polygonRenderer.startFrame(g);
    for (int i = 0; i < polygons.size(); i++) {
      polygonRenderer.draw(g, (Polygon3D) polygons.get(i));
    }
    polygonRenderer.endFrame(g);
  }

  public void drawText(Graphics2D g) {

    // draw text
    if (drawInstructions) {
      // fade out the text over 500 ms
      long fade = INSTRUCTIONS_TIME - drawInstructionsTime;
      if (fade < 500) {
        fade = fade * 255 / 500;
        g.setColor(new Color(0xffffff | ((int) fade << 24), true));
      } else {
        g.setColor(Color.WHITE);
      }

      g.drawString("Use the mouse/arrow keys to move. "
          + "Press Esc to exit.", 5, fontSize);
    }
    // (you may have to turn off the BufferStrategy in
    // ScreenManager for more accurate tests)
    if (drawFrameRate) {
      g.setColor(Color.WHITE);
      calcFrameRate();
      g.drawString(frameRate + " frames/sec", 5, screen.getHeight() - 5);
    }
  }

  public void calcFrameRate() {
    numFrames++;
    long currTime = System.currentTimeMillis();

    // calculate the frame rate every 500 milliseconds
    if (currTime > startTime + 500) {
      frameRate = (float) numFrames * 1000 / (currTime - startTime);
      startTime = currTime;
      numFrames = 0;
    }
  }

}

/**
 * The InputManager manages input of key and mouse events. Events are mapped to
 * GameActions.
 */

class InputManager implements KeyListener, MouseListener, MouseMotionListener,
    MouseWheelListener {
  /**
   * An invisible cursor.
   */
  public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit()
      .createCustomCursor(Toolkit.getDefaultToolkit().getImage(""),
          new Point(0, 0), "invisible");

  // mouse codes
  public static final int MOUSE_MOVE_LEFT = 0;

  public static final int MOUSE_MOVE_RIGHT = 1;

  public static final int MOUSE_MOVE_UP = 2;

  public static final int MOUSE_MOVE_DOWN = 3;

  public static final int MOUSE_WHEEL_UP = 4;

  public static final int MOUSE_WHEEL_DOWN = 5;

  public static final int MOUSE_BUTTON_1 = 6;

  public static final int MOUSE_BUTTON_2 = 7;

  public static final int MOUSE_BUTTON_3 = 8;

  private static final int NUM_MOUSE_CODES = 9;

  // key codes are defined in java.awt.KeyEvent.
  // most of the codes (except for some rare ones like
  // "alt graph") are less than 600.
  private static final int NUM_KEY_CODES = 600;

  private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];

  private GameAction[] mouseActions = new GameAction[NUM_MOUSE_CODES];

  private Point mouseLocation;

  private Point centerLocation;

  private Component comp;

  private Robot robot;

  private boolean isRecentering;

  /**
   * Creates a new InputManager that listens to input from the specified
   * component.
   */
  public InputManager(Component comp) {
    this.comp = comp;
    mouseLocation = new Point();
    centerLocation = new Point();

    // register key and mouse listeners
    comp.addKeyListener(this);
    comp.addMouseListener(this);
    comp.addMouseMotionListener(this);
    comp.addMouseWheelListener(this);

    // allow input of the TAB key and other keys normally
    // used for focus traversal
    comp.setFocusTraversalKeysEnabled(false);
  }

  /**
   * Sets the cursor on this InputManager's input component.
   */
  public void setCursor(Cursor cursor) {
    comp.setCursor(cursor);
  }

  /**
   * Sets whether realtive mouse mode is on or not. For relative mouse mode,
   * the mouse is "locked" in the center of the screen, and only the changed
   * in mouse movement is measured. In normal mode, the mouse is free to move
   * about the screen.
   */
  public void setRelativeMouseMode(boolean mode) {
    if (mode == isRelativeMouseMode()) {
      return;
    }

    if (mode) {
      try {
        robot = new Robot();
        mouseLocation.x = comp.getWidth() / 2;
        mouseLocation.y = comp.getHeight() / 2;
        recenterMouse();
      } catch (AWTException ex) {
        // couldn't create robot!
        robot = null;
      }
    } else {
      robot = null;
    }
  }

  /**
   * Returns whether or not relative mouse mode is on.
   */
  public boolean isRelativeMouseMode() {
    return (robot != null);
  }

  /**
   * Maps a GameAction to a specific key. The key codes are defined in
   * java.awt.KeyEvent. If the key already has a GameAction mapped to it, the
   * new GameAction overwrites it.
   */
  public void mapToKey(GameAction gameAction, int keyCode) {
    keyActions[keyCode] = gameAction;
  }

  /**
   * Maps a GameAction to a specific mouse action. The mouse codes are defined
   * herer in InputManager (MOUSE_MOVE_LEFT, MOUSE_BUTTON_1, etc). If the
   * mouse action already has a GameAction mapped to it, the new GameAction
   * overwrites it.
   */
  public void mapToMouse(GameAction gameAction, int mouseCode) {
    mouseActions[mouseCode] = gameAction;
  }

  /**
   * Clears all mapped keys and mouse actions to this GameAction.
   */
  public void clearMap(GameAction gameAction) {
    for (int i = 0; i < keyActions.length; i++) {
      if (keyActions[i] == gameAction) {
        keyActions[i] = null;
      }
    }

    for (int i = 0; i < mouseActions.length; i++) {
      if (mouseActions[i] == gameAction) {
        mouseActions[i] = null;
      }
    }

    gameAction.reset();
  }

  /**
   * Gets a List of names of the keys and mouse actions mapped to this
   * GameAction. Each entry in the List is a String.
   */
  public List getMaps(GameAction gameCode) {
    ArrayList list = new ArrayList();

    for (int i = 0; i < keyActions.length; i++) {
      if (keyActions[i] == gameCode) {
        list.add(getKeyName(i));
      }
    }

    for (int i = 0; i < mouseActions.length; i++) {
      if (mouseActions[i] == gameCode) {
        list.add(getMouseName(i));
      }
    }
    return list;
  }

  /**
   * Resets all GameActions so they appear like they haven't been pressed.
   */
  public void resetAllGameActions() {
    for (int i = 0; i < keyActions.length; i++) {
      if (keyActions[i] != null) {
        keyActions[i].reset();
      }
    }

    for (int i = 0; i < mouseActions.length; i++) {
      if (mouseActions[i] != null) {
        mouseActions[i].reset();
      }
    }
  }

  /**
   * Gets the name of a key code.
   */
  public static String getKeyName(int keyCode) {
    return KeyEvent.getKeyText(keyCode);
  }

  /**
   * Gets the name of a mouse code.
   */
  public static String getMouseName(int mouseCode) {
    switch (mouseCode) {
    case MOUSE_MOVE_LEFT:
      return "Mouse Left";
    case MOUSE_MOVE_RIGHT:
      return "Mouse Right";
    case MOUSE_MOVE_UP:
      return "Mouse Up";
    case MOUSE_MOVE_DOWN:
      return "Mouse Down";
    case MOUSE_WHEEL_UP:
      return "Mouse Wheel Up";
    case MOUSE_WHEEL_DOWN:
      return "Mouse Wheel Down";
    case MOUSE_BUTTON_1:
      return "Mouse Button 1";
    case MOUSE_BUTTON_2:
      return "Mouse Button 2";
    case MOUSE_BUTTON_3:
      return "Mouse Button 3";
    default:
      return "Unknown mouse code " + mouseCode;
    }
  }

  /**
   * Gets the x position of the mouse.
   */
  public int getMouseX() {
    return mouseLocation.x;
  }

  /**
   * Gets the y position of the mouse.
   */
  public int getMouseY() {
    return mouseLocation.y;
  }

  /**
   * Uses the Robot class to try to postion the mouse in the center of the
   * screen.
   * <p>
   * Note that use of the Robot class may not be available on all platforms.
   */
  private synchronized void recenterMouse() {
    if (robot != null && comp.isShowing()) {
      centerLocation.x = comp.getWidth() / 2;
      centerLocation.y = comp.getHeight() / 2;
      SwingUtilities.convertPointToScreen(centerLocation, comp);
      isRecentering = true;
      robot.mouseMove(centerLocation.x, centerLocation.y);
    }
  }

  private GameAction getKeyAction(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode < keyActions.length) {
      return keyActions[keyCode];
    } else {
      return null;
    }
  }

  /**
   * Gets the mouse code for the button specified in this MouseEvent.
   */
  public static int getMouseButtonCode(MouseEvent e) {
    switch (e.getButton()) {
    case MouseEvent.BUTTON1:
      return MOUSE_BUTTON_1;
    case MouseEvent.BUTTON2:
      return MOUSE_BUTTON_2;
    case MouseEvent.BUTTON3:
      return MOUSE_BUTTON_3;
    default:
      return -1;
    }
  }

  private GameAction getMouseButtonAction(MouseEvent e) {
    int mouseCode = getMouseButtonCode(e);
    if (mouseCode != -1) {
      return mouseActions[mouseCode];
    } else {
      return null;
    }
  }

  // from the KeyListener interface
  public void keyPressed(KeyEvent e) {
    GameAction gameAction = getKeyAction(e);
    if (gameAction != null) {
      gameAction.press();
    }
    // make sure the key isn't processed for anything else
    e.consume();
  }

  // from the KeyListener interface
  public void keyReleased(KeyEvent e) {
    GameAction gameAction = getKeyAction(e);
    if (gameAction != null) {
      gameAction.release();
    }
    // make sure the key isn't processed for anything else
    e.consume();
  }

  // from the KeyListener interface
  public void keyTyped(KeyEvent e) {
    // make sure the key isn't processed for anything else
    e.consume();
  }

  // from the MouseListener interface
  public void mousePressed(MouseEvent e) {
    GameAction gameAction = getMouseButtonAction(e);
    if (gameAction != null) {
      gameAction.press();
    }
  }

  // from the MouseListener interface
  public void mouseReleased(MouseEvent e) {
    GameAction gameAction = getMouseButtonAction(e);
    if (gameAction != null) {
      gameAction.release();
    }
  }

  // from the MouseListener interface
  public void mouseClicked(MouseEvent e) {
    // do nothing
  }

  // from the MouseListener interface
  public void mouseEntered(MouseEvent e) {
    mouseMoved(e);
  }

  // from the MouseListener interface
  public void mouseExited(MouseEvent e) {
    mouseMoved(e);
  }

  // from the MouseMotionListener interface
  public void mouseDragged(MouseEvent e) {
    mouseMoved(e);
  }

  // from the MouseMotionListener interface
  public synchronized void mouseMoved(MouseEvent e) {
    // this event is from re-centering the mouse - ignore it
    if (isRecentering && centerLocation.x == e.getX()
        && centerLocation.y == e.getY()) {
      isRecentering = false;
    } else {
      int dx = e.getX() - mouseLocation.x;
      int dy = e.getY() - mouseLocation.y;
      mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
      mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);

      if (isRelativeMouseMode()) {
        recenterMouse();
      }
    }

    mouseLocation.x = e.getX();
    mouseLocation.y = e.getY();

  }

  // from the MouseWheelListener interface
  public void mouseWheelMoved(MouseWheelEvent e) {
    mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
  }

  private void mouseHelper(int codeNeg, int codePos, int amount) {
    GameAction gameAction;
    if (amount < 0) {
      gameAction = mouseActions[codeNeg];
    } else {
      gameAction = mouseActions[codePos];
    }
    if (gameAction != null) {
      gameAction.press(Math.abs(amount));
      gameAction.release();
    }
  }

}

/**
 * Simple abstract class used for testing. Subclasses should implement the
 * draw() method.
 */

abstract class GameCore {

  protected static final int DEFAULT_FONT_SIZE = 24;

  // various lists of modes, ordered by preference
  protected static final DisplayMode[] MID_RES_MODES = {
      new DisplayMode(800, 600, 16, 0), new DisplayMode(800, 600, 32, 0),
      new DisplayMode(800, 600, 24, 0), new DisplayMode(640, 480, 16, 0),
      new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 24, 0),
      new DisplayMode(1024, 768, 16, 0),
      new DisplayMode(1024, 768, 32, 0),
      new DisplayMode(1024, 768, 24, 0), };

  protected static final DisplayMode[] LOW_RES_MODES = {
      new DisplayMode(640, 480, 16, 0), new DisplayMode(640, 480, 32, 0),
      new DisplayMode(640, 480, 24, 0), new DisplayMode(800, 600, 16, 0),
      new DisplayMode(800, 600, 32, 0), new DisplayMode(800, 600, 24, 0),
      new DisplayMode(1024, 768, 16, 0),
      new DisplayMode(1024, 768, 32, 0),
      new DisplayMode(1024, 768, 24, 0), };

  protected static final DisplayMode[] VERY_LOW_RES_MODES = {
      new DisplayMode(320, 240, 16, 0), new DisplayMode(400, 300, 16, 0),
      new DisplayMode(512, 384, 16, 0), new DisplayMode(640, 480, 16, 0),
      new DisplayMode(800, 600, 16, 0), };

  private boolean isRunning;

  protected ScreenManager screen;

  protected int fontSize = DEFAULT_FONT_SIZE;

  /**
   * Signals the game loop that it's time to quit
   */
  public void stop() {
    isRunning = false;
  }

  /**
   * Calls init() and gameLoop()
   */
  public void run() {
    try {
      init();
      gameLoop();
    } finally {
      if (screen != null) {
        screen.restoreScreen();
      }
      lazilyExit();
    }
  }

  /**
   * Exits the VM from a daemon thread. The daemon thread waits 2 seconds then
   * calls System.exit(0). Since the VM should exit when only daemon threads
   * are running, this makes sure System.exit(0) is only called if neccesary.
   * It's neccesary if the Java Sound system is running.
   */
  public void lazilyExit() {
    Thread thread = new Thread() {
      public void run() {
        // first, wait for the VM exit on its own.
        try {
          Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        // system is still running, so force an exit
        System.exit(0);
      }
    };
    thread.setDaemon(true);
    thread.start();
  }

  /**
   * Sets full screen mode and initiates and objects.
   */
  public void init() {
    init(MID_RES_MODES);
  }

  /**
   * Sets full screen mode and initiates and objects.
   */
  public void init(DisplayMode[] possibleModes) {
    screen = new ScreenManager();
    DisplayMode displayMode = screen.findFirstCompatibleMode(possibleModes);
    screen.setFullScreen(displayMode);

    Window window = screen.getFullScreenWindow();
    window.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    window.setBackground(Color.blue);
    window.setForeground(Color.white);

    isRunning = true;
  }

  public Image loadImage(String fileName) {
    return new ImageIcon(fileName).getImage();
  }

  /**
   * Runs through the game loop until stop() is called.
   */
  public void gameLoop() {
    long startTime = System.currentTimeMillis();
    long currTime = startTime;

    while (isRunning) {
      long elapsedTime = System.currentTimeMillis() - currTime;
      currTime += elapsedTime;

      // update
      update(elapsedTime);

      // draw the screen
      Graphics2D g = screen.getGraphics();
      draw(g);
      g.dispose();
      screen.update();

      // don't take a nap! run as fast as possible
      /*
       * try { Thread.sleep(20); } catch (InterruptedException ex) { }
       */
    }
  }

  /**
   * Updates the state of the game/animation based on the amount of elapsed
   * time that has passed.
   */
  public void update(long elapsedTime) {
    // do nothing
  }

  /**
   * Draws to the screen. Subclasses must override this method.
   */
  public abstract void draw(Graphics2D g);
}

/**
 * The GameObjectManager interface provides methods to keep track of and draw
 * GameObjects.
 */

interface GameObjectManager {

  /**
   * Marks all objects within the specified 2D bounds as potentially visible
   * (should be drawn).
   */
  public void markVisible(Rectangle bounds);

  /**
   * Marks all objects as potentially visible (should be drawn).
   */
  public void markAllVisible();

  /**
   * Adds a GameObject to this manager.
   */
  public void add(GameObject object);

  /**
   * Adds a GameObject to this manager, specifying it as the player object. An
   * existing player object, if any, is not removed.
   */
  public void addPlayer(GameObject player);

  /**
   * Gets the object specified as the Player object, or null if no player
   * object was specified.
   */
  public GameObject getPlayer();

  /**
   * Removes a GameObject from this manager.
   */
  public void remove(GameObject object);

  /**
   * Updates all objects based on the amount of time passed from the last
   * update.
   */
  public void update(long elapsedTime);

  /**
   * Draws all visible objects.
   */
  public void draw(Graphics2D g, GameObjectRenderer r);

}

/**
 * The GameObjectRenderer interface provides a method for drawing a GameObject.
 */

interface GameObjectRenderer {

  /**
   * Draws the object and returns true if any part of the object is visible.
   */
  public boolean draw(Graphics2D g, GameObject object);

}

/**
 * The Blast GameObject is a projectile, designed to travel in a straight line
 * for five seconds, then die. Blasts destroy Bots instantly.
 */

class Blast extends GameObject {

  private static final long DIE_TIME = 5000;

  private static final float SPEED = 1.5f;

  private static final float ROT_SPEED = .008f;

  private long aliveTime;

  /**
   * Create a new Blast with the specified PolygonGroup and normalized vector
   * direction.
   */
  public Blast(PolygonGroup polygonGroup, Vector3D direction) {
    super(polygonGroup);
    MovingTransform3D transform = polygonGroup.getTransform();
    Vector3D velocity = transform.getVelocity();
    velocity.setTo(direction);
    velocity.multiply(SPEED);
    transform.setVelocity(velocity);
    //transform.setAngleVelocityX(ROT_SPEED);
    transform.setAngleVelocityY(ROT_SPEED);
    transform.setAngleVelocityZ(ROT_SPEED);
    setState(STATE_ACTIVE);
  }

  public void update(GameObject player, long elapsedTime) {
    aliveTime += elapsedTime;
    if (aliveTime >= DIE_TIME) {
      setState(STATE_DESTROYED);
    } else {
      super.update(player, elapsedTime);
    }
  }

}

abstract class ShooterCore extends GameCore3D {

  private static final float PLAYER_SPEED = .5f;

  private static final float PLAYER_TURN_SPEED = 0.04f;

  private static final float CAMERA_HEIGHT = 100;

  private static final float BULLET_HEIGHT = 75;

  protected GameAction fire = new GameAction("fire",
      GameAction.DETECT_INITAL_PRESS_ONLY);

  protected GameObjectManager gameObjectManager;

  protected PolygonGroup blastModel;

  protected DisplayMode[] modes;

  public ShooterCore(String[] args) {
    modes = LOW_RES_MODES;
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-lowres")) {
        modes = VERY_LOW_RES_MODES;
        fontSize = 12;
      }
    }
  }

  public void init() {

    init(modes);

    inputManager.mapToKey(fire, KeyEvent.VK_SPACE);
    inputManager.mapToMouse(fire, InputManager.MOUSE_BUTTON_1);

    // set up the local lights for the model.
    float ambientLightIntensity = .8f;
    List lights = new LinkedList();
    lights.add(new PointLight3D(-100, 100, 100, .5f, -1));
    lights.add(new PointLight3D(100, 100, 0, .5f, -1));

    // load the object model
    ObjectLoader loader = new ObjectLoader();
    loader.setLights(lights, ambientLightIntensity);
    try {
      blastModel = loader.loadObject("../images/blast.obj");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void createPolygonRenderer() {
    // make the view window the entire screen
    viewWindow = new ViewWindow(0, 0, screen.getWidth(),
        screen.getHeight(), (float) Math.toRadians(75));

    Transform3D camera = new Transform3D();
    polygonRenderer = new BSPRenderer(camera, viewWindow);
  }

  public void updateWorld(long elapsedTime) {

    float angleVelocity;

    // cap elapsedTime
    elapsedTime = Math.min(elapsedTime, 100);

    GameObject player = gameObjectManager.getPlayer();
    MovingTransform3D playerTransform = player.getTransform();
    Vector3D velocity = playerTransform.getVelocity();

    //playerTransform.stop();
    velocity.x = 0;
    velocity.z = 0;
    float x = -playerTransform.getSinAngleY();
    float z = -playerTransform.getCosAngleY();
    if (goForward.isPressed()) {
      velocity.add(x * PLAYER_SPEED, 0, z * PLAYER_SPEED);
    }
    if (goBackward.isPressed()) {
      velocity.add(-x * PLAYER_SPEED, 0, -z * PLAYER_SPEED);
    }
    if (goLeft.isPressed()) {
      velocity.add(z * PLAYER_SPEED, 0, -x * PLAYER_SPEED);
    }
    if (goRight.isPressed()) {
      velocity.add(-z * PLAYER_SPEED, 0, x * PLAYER_SPEED);
    }
    if (fire.isPressed()) {
      float cosX = playerTransform.getCosAngleX();
      float sinX = playerTransform.getSinAngleX();
      Blast blast = new Blast((PolygonGroup) blastModel.clone(),
          new Vector3D(cosX * x, sinX, cosX * z));
      // blast starting location needs work. looks like
      // the blast is coming out of your forehead when
      // you're shooting down.
      blast.getLocation().setTo(player.getX(),
          player.getY() + BULLET_HEIGHT, player.getZ());
      gameObjectManager.add(blast);
    }

    playerTransform.setVelocity(velocity);

    // look up/down (rotate around x)
    angleVelocity = Math.min(tiltUp.getAmount(), 200);
    angleVelocity += Math.max(-tiltDown.getAmount(), -200);
    playerTransform.setAngleVelocityX(angleVelocity * PLAYER_TURN_SPEED
        / 200);

    // turn (rotate around y)
    angleVelocity = Math.min(turnLeft.getAmount(), 200);
    angleVelocity += Math.max(-turnRight.getAmount(), -200);
    playerTransform.setAngleVelocityY(angleVelocity * PLAYER_TURN_SPEED
        / 200);

    // update objects
    gameObjectManager.update(elapsedTime);

    // limit look up/down
    float angleX = playerTransform.getAngleX();
    float limit = (float) Math.PI / 2;
    if (angleX < -limit) {
      playerTransform.setAngleX(-limit);
    } else if (angleX > limit) {
      playerTransform.setAngleX(limit);
    }

    // set the camera to be 100 units above the player
    Transform3D camera = polygonRenderer.getCamera();
    camera.setTo(playerTransform);
    camera.getLocation().add(0, CAMERA_HEIGHT, 0);

  }

}

/**
 * The Texture class is an sabstract class that represents a 16-bit color
 * texture.
 */

abstract class Texture {

  protected int width;

  protected int height;

  /**
   * Creates a new Texture with the specified width and height.
   */
  public Texture(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Gets the width of this Texture.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of this Texture.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the 16-bit color of this Texture at the specified (x,y) location.
   */
  public abstract short getColor(int x, int y);

  /**
   * Creates an unshaded Texture from the specified image file.
   */
  public static Texture createTexture(String filename) {
    return createTexture(filename, false);
  }

  /**
   * Creates an Texture from the specified image file. If shaded is true, then
   * a ShadedTexture is returned.
   */
  public static Texture createTexture(String filename, boolean shaded) {
    try {
      return createTexture(ImageIO.read(new File(filename)), shaded);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Creates an unshaded Texture from the specified image.
   */
  public static Texture createTexture(BufferedImage image) {
    return createTexture(image, false);
  }

  /**
   * Creates an Texture from the specified image. If shaded is true, then a
   * ShadedTexture is returned.
   */
  public static Texture createTexture(BufferedImage image, boolean shaded) {
    int type = image.getType();
    int width = image.getWidth();
    int height = image.getHeight();

    if (!isPowerOfTwo(width) || !isPowerOfTwo(height)) {
      throw new IllegalArgumentException(
          "Size of texture must be a power of two.");
    }

    if (shaded) {
      // convert image to an indexed image
      if (type != BufferedImage.TYPE_BYTE_INDEXED) {
        System.out.println("Warning: image converted to "
            + "256-color indexed image. Some quality may "
            + "be lost.");
        BufferedImage newImage = new BufferedImage(image.getWidth(),
            image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = newImage;
      }
      DataBuffer dest = image.getRaster().getDataBuffer();
      return new ShadedTexture(((DataBufferByte) dest).getData(),
          countbits(width - 1), countbits(height - 1),
          (IndexColorModel) image.getColorModel());
    } else {
      // convert image to an 16-bit image
      if (type != BufferedImage.TYPE_USHORT_565_RGB) {
        BufferedImage newImage = new BufferedImage(image.getWidth(),
            image.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = newImage;
      }

      DataBuffer dest = image.getRaster().getDataBuffer();
      return new PowerOf2Texture(((DataBufferUShort) dest).getData(),
          countbits(width - 1), countbits(height - 1));
    }
  }

  /**
   * Returns true if the specified number is a power of 2.
   */
  public static boolean isPowerOfTwo(int n) {
    return ((n & (n - 1)) == 0);
  }

  /**
   * Counts the number of "on" bits in an integer.
   */
  public static int countbits(int n) {
    int count = 0;
    while (n > 0) {
      count += (n & 1);
      n >>= 1;
    }
    return count;
  }
}

/**
 * The GameAction class is an abstract to a user-initiated action, like jumping
 * or moving. GameActions can be mapped to keys or the mouse with the
 * InputManager.
 */

class GameAction {

  /**
   * Normal behavior. The isPressed() method returns true as long as the key
   * is held down.
   */
  public static final int NORMAL = 0;

  /**
   * Initial press behavior. The isPressed() method returns true only after
   * the key is first pressed, and not again until the key is released and
   * pressed again.
   */
  public static final int DETECT_INITAL_PRESS_ONLY = 1;

  private static final int STATE_RELEASED = 0;

  private static final int STATE_PRESSED = 1;

  private static final int STATE_WAITING_FOR_RELEASE = 2;

  private String name;

  private int behavior;

  private int amount;

  private int state;

  /**
   * Create a new GameAction with the NORMAL behavior.
   */
  public GameAction(String name) {
    this(name, NORMAL);
  }

  /**
   * Create a new GameAction with the specified behavior.
   */
  public GameAction(String name, int behavior) {
    this.name = name;
    this.behavior = behavior;
    reset();
  }

  /**
   * Gets the name of this GameAction.
   */
  public String getName() {
    return name;
  }

  /**
   * Resets this GameAction so that it appears like it hasn't been pressed.
   */
  public void reset() {
    state = STATE_RELEASED;
    amount = 0;
  }

  /**
   * Taps this GameAction. Same as calling press() followed by release().
   */
  public synchronized void tap() {
    press();
    release();
  }

  /**
   * Signals that the key was pressed.
   */
  public synchronized void press() {
    press(1);
  }

  /**
   * Signals that the key was pressed a specified number of times, or that the
   * mouse move a spcified distance.
   */
  public synchronized void press(int amount) {
    if (state != STATE_WAITING_FOR_RELEASE) {
      this.amount += amount;
      state = STATE_PRESSED;
    }

  }

  /**
   * Signals that the key was released
   */
  public synchronized void release() {
    state = STATE_RELEASED;
  }

  /**
   * Returns whether the key was pressed or not since last checked.
   */
  public synchronized boolean isPressed() {
    return (getAmount() != 0);
  }

  /**
   * For keys, this is the number of times the key was pressed since it was
   * last checked. For mouse movement, this is the distance moved.
   */
  public synchronized int getAmount() {
    int retVal = amount;
    if (retVal != 0) {
      if (state == STATE_RELEASED) {
        amount = 0;
      } else if (behavior == DETECT_INITAL_PRESS_ONLY) {
        state = STATE_WAITING_FOR_RELEASE;
        amount = 0;
      }
    }
    return retVal;
  }
}

/**
 * A PointLight3D is a point light that has an intensity (between 0 and 1) and
 * optionally a distance falloff value, which causes the light to diminish with
 * distance.
 */

class PointLight3D extends Vector3D {

  public static final float NO_DISTANCE_FALLOFF = -1;

  private float intensity;

  private float distanceFalloff;

  /**
   * Creates a new PointLight3D at (0,0,0) with an intensity of 1 and no
   * distance falloff.
   */
  public PointLight3D() {
    this(0, 0, 0, 1, NO_DISTANCE_FALLOFF);
  }

  /**
   * Creates a copy of the specified PointLight3D.
   */
  public PointLight3D(PointLight3D p) {
    setTo(p);
  }

  /**
   * Creates a new PointLight3D with the specified location and intensity. The
   * created light has no distance falloff.
   */
  public PointLight3D(float x, float y, float z, float intensity) {
    this(x, y, z, intensity, NO_DISTANCE_FALLOFF);
  }

  /**
   * Creates a new PointLight3D with the specified location. intensity, and no
   * distance falloff.
   */
  public PointLight3D(float x, float y, float z, float intensity,
      float distanceFalloff) {
    setTo(x, y, z);
    setIntensity(intensity);
    setDistanceFalloff(distanceFalloff);
  }

  /**
   * Sets this PointLight3D to the same location, intensity, and distance
   * falloff as the specified PointLight3D.
   */
  public void setTo(PointLight3D p) {
    setTo(p.x, p.y, p.z);
    setIntensity(p.getIntensity());
    setDistanceFalloff(p.getDistanceFalloff());
  }

  /**
   * Gets the intensity of this light from the specified distance.
   */
  public float getIntensity(float distance) {
    if (distanceFalloff == NO_DISTANCE_FALLOFF) {
      return intensity;
    } else if (distance >= distanceFalloff) {
      return 0;
    } else {
      return intensity * (distanceFalloff - distance)
          / (distanceFalloff + distance);
    }
  }

  /**
   * Gets the intensity of this light.
   */
  public float getIntensity() {
    return intensity;
  }

  /**
   * Sets the intensity of this light.
   */
  public void setIntensity(float intensity) {
    this.intensity = intensity;
  }

  /**
   * Gets the distances falloff value. The light intensity is zero beyond this
   * distance.
   */
  public float getDistanceFalloff() {
    return distanceFalloff;
  }

  /**
   * Sets the distances falloff value. The light intensity is zero beyond this
   * distance. Set to NO_DISTANCE_FALLOFF if the light does not diminish with
   * distance.
   */
  public void setDistanceFalloff(float distanceFalloff) {
    this.distanceFalloff = distanceFalloff;
  }

}

/**
 * The SimpleGameObjectManager is a GameObjectManager that keeps all object in a
 * list and performs no collision detection.
 */

class SimpleGameObjectManager implements GameObjectManager {

  private List allObjects;

  private List visibleObjects;

  private GameObject player;

  /**
   * Creates a new SimpleGameObjectManager.
   */
  public SimpleGameObjectManager() {
    allObjects = new ArrayList();
    visibleObjects = new ArrayList();
    player = null;
  }

  /**
   * Marks all objects as potentially visible (should be drawn).
   */
  public void markAllVisible() {
    for (int i = 0; i < allObjects.size(); i++) {
      GameObject object = (GameObject) allObjects.get(i);
      if (!visibleObjects.contains(object)) {
        visibleObjects.add(object);
      }
    }
  }

  /**
   * Marks all objects within the specified 2D bounds as potentially visible
   * (should be drawn).
   */
  public void markVisible(Rectangle bounds) {
    for (int i = 0; i < allObjects.size(); i++) {
      GameObject object = (GameObject) allObjects.get(i);
      if (bounds.contains(object.getX(), object.getZ())
          && !visibleObjects.contains(object)) {
        visibleObjects.add(object);
      }
    }
  }

  /**
   * Adds a GameObject to this manager.
   */
  public void add(GameObject object) {
    if (object != null) {
      allObjects.add(object);
    }
  }

  /**
   * Adds a GameObject to this manager, specifying it as the player object. An
   * existing player object, if any, is not removed.
   */
  public void addPlayer(GameObject player) {
    this.player = player;
    if (player != null) {
      player.notifyVisible(true);
      allObjects.add(0, player);
    }
  }

  /**
   * Gets the object specified as the Player object, or null if no player
   * object was specified.
   */
  public GameObject getPlayer() {
    return player;
  }

  /**
   * Removes a GameObject from this manager.
   */
  public void remove(GameObject object) {
    allObjects.remove(object);
    visibleObjects.remove(object);
  }

  /**
   * Updates all objects based on the amount of time passed from the last
   * update.
   */
  public void update(long elapsedTime) {
    for (int i = 0; i < allObjects.size(); i++) {
      GameObject object = (GameObject) allObjects.get(i);
      object.update(player, elapsedTime);

      // remove destroyed objects
      if (object.isDestroyed()) {
        allObjects.remove(i);
        visibleObjects.remove(object);
        i--;
      }
    }
  }

  /**
   * Draws all visible objects and marks all objects as not visible.
   */
  public void draw(Graphics2D g, GameObjectRenderer r) {
    Iterator i = visibleObjects.iterator();
    while (i.hasNext()) {
      GameObject object = (GameObject) i.next();
      boolean visible = r.draw(g, object);
      // notify objects if they are visible this frame
      object.notifyVisible(visible);
    }
    visibleObjects.clear();
  }
}
/**
 * A GameObject class is a base class for any type of object in a game that is
 * represented by a PolygonGroup. For example, a GameObject can be a static
 * object (like a crate), a moving object (like a projectile or a bad guy), or
 * any other type of object (like a power-ups). GameObjects have three basic
 * states: STATE_IDLE, STATE_ACTIVE, or STATE_DESTROYED.
 */

class GameObject {

  /**
   * Represents a GameObject that is idle. If the object is idle, it's
   * Transform3D is not updated. By default, GameObjects are initially idle
   * and are changed to the active state when they are initially visible. This
   * behavior can be changed by overriding the notifyVisible() method.
   */
  protected static final int STATE_IDLE = 0;

  /**
   * Represents a GameObject that is active. should no longer be updated or
   * drawn. Once in the STATE_DESTROYED state, the GameObjectManager should
   * remove this object from the list of GameObject it manages.
   */
  protected static final int STATE_ACTIVE = 1;

  /**
   * Represents a GameObject that has been destroyed, and should no longer be
   * updated or drawn. Once in the STATE_DESTROYED state, the
   * GameObjectManager should remove this object from the list of GameObject
   * it manages.
   */
  protected static final int STATE_DESTROYED = 2;

  private PolygonGroup polygonGroup;

  private PolygonGroupBounds bounds;

  private int state;

  private boolean isJumping;

  private float floorHeight;

  private float ceilHeight;

  /**
   * Creates a new GameObject represented by the specified PolygonGroup. The
   * PolygonGroup can be null.
   */
  public GameObject(PolygonGroup polygonGroup) {
    this.polygonGroup = polygonGroup;
    bounds = new PolygonGroupBounds(polygonGroup);
    state = STATE_IDLE;
  }

  /**
   * Shortcut to get the location of this GameObject from the Transform3D.
   */
  public Vector3D getLocation() {
    return polygonGroup.getTransform().getLocation();
  }

  /**
   * Gets this object's transform.
   */
  public MovingTransform3D getTransform() {
    return polygonGroup.getTransform();
  }

  /**
   * Gets this object's PolygonGroup.
   */
  public PolygonGroup getPolygonGroup() {
    return polygonGroup;
  }

  /**
   * Gets the bounds of this object's PolygonGroup.
   */
  public PolygonGroupBounds getBounds() {
    return bounds;
  }

  /**
   * Shortcut to get the X location of this GameObject.
   */
  public float getX() {
    return getLocation().x;
  }

  /**
   * Shortcut to get the Y location of this GameObject.
   */
  public float getY() {
    return getLocation().y;
  }

  /**
   * Shortcut to get the Z location of this GameObject.
   */
  public float getZ() {
    return getLocation().z;
  }

  /**
   * Method to record the height of the floor that this GameObject is on.
   */
  public void setFloorHeight(float floorHeight) {
    this.floorHeight = floorHeight;
  }

  /**
   * Method to record the height of the ceiling that this GameObject is under.
   */
  public void setCeilHeight(float ceilHeight) {
    this.ceilHeight = ceilHeight;
  }

  /**
   * Gets the floor height set in the setFloorHeight method.
   */
  public float getFloorHeight() {
    return floorHeight;
  }

  /**
   * Gets the ceiling height set in the setCeilHeight method.
   */
  public float getCeilHeight() {
    return ceilHeight;
  }

  /**
   * Sets the state of this object. Should be either STATE_IDLE, STATE_ACTIVE,
   * or STATE_DESTROYED.
   */
  protected void setState(int state) {
    this.state = state;
  }

  /**
   * Sets the state of the specified object. This allows any GameObject to set
   * the state of any other GameObject. The state should be either STATE_IDLE,
   * STATE_ACTIVE, or STATE_DESTROYED.
   */
  protected void setState(GameObject object, int state) {
    object.setState(state);
  }

  /**
   * Checks if this GameObject is currently flying. Flying objects should not
   * has gravity applied to them. Returns false by default.
   */
  public boolean isFlying() {
    return false;
  }

  /**
   * Checks if this GameObject's jumping flag is set. The GameObjectManager
   * may treat the object differently if it is jumping.
   */
  public boolean isJumping() {
    return isJumping;
  }

  /**
   * Sets this GameObject's jumping flag. The GameObjectManager may treat the
   * object differently if it is jumping.
   */
  public void setJumping(boolean b) {
    isJumping = b;
  }

  /**
   * Returns true if this GameObject is idle.
   */
  public boolean isIdle() {
    return (state == STATE_IDLE);
  }

  /**
   * Returns true if this GameObject is active.
   */
  public boolean isActive() {
    return (state == STATE_ACTIVE);
  }

  /**
   * Returns true if this GameObject is destroyed.
   */
  public boolean isDestroyed() {
    return (state == STATE_DESTROYED);
  }

  /**
   * If this GameObject is in the active state, this method updates it's
   * PolygonGroup. Otherwise, this method does nothing.
   */
  public void update(GameObject player, long elapsedTime) {
    if (isActive()) {
      polygonGroup.update(elapsedTime);
    }
  }

  /**
   * Notifies this GameObject whether it was visible or not on the last
   * update. By default, if this GameObject is idle and notified as visible,
   * it changes to the active state.
   */
  public void notifyVisible(boolean visible) {
    if (visible && isIdle()) {
      state = STATE_ACTIVE;
    }
  }

  /**
   * Notifies this GameObject that when it moved, it collided with the
   * specified object. Does nothing by default.
   */
  public void notifyObjectCollision(GameObject otherObject) {
    // do nothing
  }

  /**
   * Notifies this GameObject that when it moved, it collided with a floor.
   * Does nothing by default.
   */
  public void notifyFloorCollision() {
    // do nothing
  }

  /**
   * Notifies this GameObject that when it moved, it collided with a ceiling.
   * Does nothing by default.
   */
  public void notifyCeilingCollision() {
    // do nothing
  }

  /**
   * Notifies this GameObject that when it moved, it collided with a wall.
   * Does nothing by default.
   */
  public void notifyWallCollision() {
    // do nothing
  }

}

/**
 * The Vector3D class implements a 3D vector with the floating-point values x,
 * y, and z. Vectors can be thought of either as a (x,y,z) point or as a vector
 * from (0,0,0) to (x,y,z).
 */

class Vector3D implements Transformable {

  public float x;

  public float y;

  public float z;

  /**
   * Creates a new Vector3D at (0,0,0).
   */
  public Vector3D() {
    this(0, 0, 0);
  }

  /**
   * Creates a new Vector3D with the same values as the specified Vector3D.
   */
  public Vector3D(Vector3D v) {
    this(v.x, v.y, v.z);
  }

  /**
   * Creates a new Vector3D with the specified (x, y, z) values.
   */
  public Vector3D(float x, float y, float z) {
    setTo(x, y, z);
  }

  /**
   * Checks if this Vector3D is equal to the specified Object. They are equal
   * only if the specified Object is a Vector3D and the two Vector3D's x, y,
   * and z coordinates are equal.
   */
  public boolean equals(Object obj) {
    Vector3D v = (Vector3D) obj;
    return (v.x == x && v.y == y && v.z == z);
  }

  /**
   * Checks if this Vector3D is equal to the specified x, y, and z
   * coordinates.
   */
  public boolean equals(float x, float y, float z) {
    return (this.x == x && this.y == y && this.z == z);
  }

  /**
   * Sets the vector to the same values as the specified Vector3D.
   */
  public void setTo(Vector3D v) {
    setTo(v.x, v.y, v.z);
  }

  /**
   * Sets this vector to the specified (x, y, z) values.
   */
  public void setTo(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Adds the specified (x, y, z) values to this vector.
   */
  public void add(float x, float y, float z) {
    this.x += x;
    this.y += y;
    this.z += z;
  }

  /**
   * Subtracts the specified (x, y, z) values to this vector.
   */
  public void subtract(float x, float y, float z) {
    add(-x, -y, -z);
  }

  /**
   * Adds the specified vector to this vector.
   */
  public void add(Vector3D v) {
    add(v.x, v.y, v.z);
  }

  /**
   * Subtracts the specified vector from this vector.
   */
  public void subtract(Vector3D v) {
    add(-v.x, -v.y, -v.z);
  }

  /**
   * Multiplies this vector by the specified value. The new length of this
   * vector will be length()*s.
   */
  public void multiply(float s) {
    x *= s;
    y *= s;
    z *= s;
  }

  /**
   * Divides this vector by the specified value. The new length of this vector
   * will be length()/s.
   */
  public void divide(float s) {
    x /= s;
    y /= s;
    z /= s;
  }

  /**
   * Returns the length of this vector as a float.
   */
  public float length() {
    return (float) Math.sqrt(x * x + y * y + z * z);
  }

  /**
   * Converts this Vector3D to a unit vector, or in other words, a vector of
   * length 1. Same as calling v.divide(v.length()).
   */
  public void normalize() {
    divide(length());
  }

  /**
   * Converts this Vector3D to a String representation.
   */
  public String toString() {
    return "(" + x + ", " + y + ", " + z + ")";
  }

  /**
   * Rotate this vector around the x axis the specified amount. The specified
   * angle is in radians. Use Math.toRadians() to convert from degrees to
   * radians.
   */
  public void rotateX(float angle) {
    rotateX((float) Math.cos(angle), (float) Math.sin(angle));
  }

  /**
   * Rotate this vector around the y axis the specified amount. The specified
   * angle is in radians. Use Math.toRadians() to convert from degrees to
   * radians.
   */
  public void rotateY(float angle) {
    rotateY((float) Math.cos(angle), (float) Math.sin(angle));
  }

  /**
   * Rotate this vector around the z axis the specified amount. The specified
   * angle is in radians. Use Math.toRadians() to convert from degrees to
   * radians.
   */
  public void rotateZ(float angle) {
    rotateZ((float) Math.cos(angle), (float) Math.sin(angle));
  }

  /**
   * Rotate this vector around the x axis the specified amount, using
   * pre-computed cosine and sine values of the angle to rotate.
   */
  public void rotateX(float cosAngle, float sinAngle) {
    float newY = y * cosAngle - z * sinAngle;
    float newZ = y * sinAngle + z * cosAngle;
    y = newY;
    z = newZ;
  }

  /**
   * Rotate this vector around the y axis the specified amount, using
   * pre-computed cosine and sine values of the angle to rotate.
   */
  public void rotateY(float cosAngle, float sinAngle) {
    float newX = z * sinAngle + x * cosAngle;
    float newZ = z * cosAngle - x * sinAngle;
    x = newX;
    z = newZ;
  }

  /**
   * Rotate this vector around the y axis the specified amount, using
   * pre-computed cosine and sine values of the angle to rotate.
   */
  public void rotateZ(float cosAngle, float sinAngle) {
    float newX = x * cosAngle - y * sinAngle;
    float newY = x * sinAngle + y * cosAngle;
    x = newX;
    y = newY;
  }

  /**
   * Adds the specified transform to this vector. This vector is first
   * rotated, then translated.
   */
  public void add(Transform3D xform) {

    // rotate
    addRotation(xform);

    // translate
    add(xform.getLocation());
  }

  /**
   * Subtracts the specified transform to this vector. This vector translated,
   * then rotated.
   */
  public void subtract(Transform3D xform) {

    // translate
    subtract(xform.getLocation());

    // rotate
    subtractRotation(xform);
  }

  /**
   * Rotates this vector with the angle of the specified transform.
   */
  public void addRotation(Transform3D xform) {
    rotateX(xform.getCosAngleX(), xform.getSinAngleX());
    rotateZ(xform.getCosAngleZ(), xform.getSinAngleZ());
    rotateY(xform.getCosAngleY(), xform.getSinAngleY());
  }

  /**
   * Rotates this vector with the opposite angle of the specified transform.
   */
  public void subtractRotation(Transform3D xform) {
    // note that sin(-x) == -sin(x) and cos(-x) == cos(x)
    rotateY(xform.getCosAngleY(), -xform.getSinAngleY());
    rotateZ(xform.getCosAngleZ(), -xform.getSinAngleZ());
    rotateX(xform.getCosAngleX(), -xform.getSinAngleX());
  }

  /**
   * Returns the dot product of this vector and the specified vector.
   */
  public float getDotProduct(Vector3D v) {
    return x * v.x + y * v.y + z * v.z;
  }

  /**
   * Sets this vector to the cross product of the two specified vectors.
   * Either of the specified vectors can be this vector.
   */
  public void setToCrossProduct(Vector3D u, Vector3D v) {
    // assign to local vars first in case u or v is 'this'
    float x = u.y * v.z - u.z * v.y;
    float y = u.z * v.x - u.x * v.z;
    float z = u.x * v.y - u.y * v.x;
    this.x = x;
    this.y = y;
    this.z = z;
  }

}

/**
 * The ScreenManager class manages initializing and displaying full screen
 * graphics modes.
 */

class ScreenManager {

  private GraphicsDevice device;

  /**
   * Creates a new ScreenManager object.
   */
  public ScreenManager() {
    GraphicsEnvironment environment = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    device = environment.getDefaultScreenDevice();
  }

  /**
   * Returns a list of compatible display modes for the default device on the
   * system.
   */
  public DisplayMode[] getCompatibleDisplayModes() {
    return device.getDisplayModes();
  }

  /**
   * Returns the first compatible mode in a list of modes. Returns null if no
   * modes are compatible.
   */
  public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
    DisplayMode goodModes[] = device.getDisplayModes();
    for (int i = 0; i < modes.length; i++) {
      for (int j = 0; j < goodModes.length; j++) {
        if (displayModesMatch(modes[i], goodModes[j])) {
          return modes[i];
        }
      }

    }

    return null;
  }

  /**
   * Returns the current display mode.
   */
  public DisplayMode getCurrentDisplayMode() {
    return device.getDisplayMode();
  }

  /**
   * Determines if two display modes "match". Two display modes match if they
   * have the same resolution, bit depth, and refresh rate. The bit depth is
   * ignored if one of the modes has a bit depth of
   * DisplayMode.BIT_DEPTH_MULTI. Likewise, the refresh rate is ignored if one
   * of the modes has a refresh rate of DisplayMode.REFRESH_RATE_UNKNOWN.
   */
  public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2)

  {
    if (mode1.getWidth() != mode2.getWidth()
        || mode1.getHeight() != mode2.getHeight()) {
      return false;
    }

    if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
        && mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
        && mode1.getBitDepth() != mode2.getBitDepth()) {
      return false;
    }

    if (mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
        && mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
        && mode1.getRefreshRate() != mode2.getRefreshRate()) {
      return false;
    }

    return true;
  }

  /**
   * Enters full screen mode and changes the display mode. If the specified
   * display mode is null or not compatible with this device, or if the
   * display mode cannot be changed on this system, the current display mode
   * is used.
   * <p>
   * The display uses a BufferStrategy with 2 buffers.
   */
  public void setFullScreen(DisplayMode displayMode) {
    final JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setUndecorated(true);
    frame.setIgnoreRepaint(true);
    frame.setResizable(false);

    device.setFullScreenWindow(frame);

    if (displayMode != null && device.isDisplayChangeSupported()) {
      try {
        device.setDisplayMode(displayMode);
      } catch (IllegalArgumentException ex) {
      }
      // fix for mac os x
      frame.setSize(displayMode.getWidth(), displayMode.getHeight());
    }
    // avoid potential deadlock in 1.4.1_02
    try {
      EventQueue.invokeAndWait(new Runnable() {
        public void run() {
          frame.createBufferStrategy(2);
        }
      });
    } catch (InterruptedException ex) {
      // ignore
    } catch (InvocationTargetException ex) {
      // ignore
    }

  }

  /**
   * Gets the graphics context for the display. The ScreenManager uses double
   * buffering, so applications must call update() to show any graphics drawn.
   * <p>
   * The application must dispose of the graphics object.
   */
  public Graphics2D getGraphics() {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      BufferStrategy strategy = window.getBufferStrategy();
      return (Graphics2D) strategy.getDrawGraphics();
    } else {
      return null;
    }
  }

  /**
   * Updates the display.
   */
  public void update() {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      BufferStrategy strategy = window.getBufferStrategy();
      if (!strategy.contentsLost()) {
        strategy.show();
      }
    }
    // Sync the display on some systems.
    // (on Linux, this fixes event queue problems)
    //Toolkit.getDefaultToolkit().sync();
  }

  /**
   * Returns the window currently used in full screen mode. Returns null if
   * the device is not in full screen mode.
   */
  public JFrame getFullScreenWindow() {
    return (JFrame) device.getFullScreenWindow();
  }

  /**
   * Returns the width of the window currently used in full screen mode.
   * Returns 0 if the device is not in full screen mode.
   */
  public int getWidth() {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      return window.getWidth();
    } else {
      return 0;
    }
  }

  /**
   * Returns the height of the window currently used in full screen mode.
   * Returns 0 if the device is not in full screen mode.
   */
  public int getHeight() {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      return window.getHeight();
    } else {
      return 0;
    }
  }

  /**
   * Restores the screen's display mode.
   */
  public void restoreScreen() {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      window.dispose();
    }
    device.setFullScreenWindow(null);
  }

  /**
   * Creates an image compatible with the current display.
   */
  public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
    Window window = device.getFullScreenWindow();
    if (window != null) {
      GraphicsConfiguration gc = window.getGraphicsConfiguration();
      return gc.createCompatibleImage(w, h, transparancy);
    }
    return null;
  }
}

/**
 * The ShadedTexture class is a Texture that has multiple shades. The texture
 * source image is stored as a 8-bit image with a palette for every shade.
 */

final class ShadedTexture extends Texture {

  public static final int NUM_SHADE_LEVELS = 64;

  public static final int MAX_LEVEL = NUM_SHADE_LEVELS - 1;

  private static final int PALETTE_SIZE_BITS = 8;

  private static final int PALETTE_SIZE = 1 << PALETTE_SIZE_BITS;

  private byte[] buffer;

  private IndexColorModel palette;

  private short[] shadeTable;

  private int defaultShadeLevel;

  private int widthBits;

  private int widthMask;

  private int heightBits;

  private int heightMask;

  // the row set in setCurrRow and used in getColorCurrRow
  private int currRow;

  /**
   * Creates a new ShadedTexture from the specified 8-bit image buffer and
   * palette. The width of the bitmap is 2 to the power of widthBits, or (1 < <
   * widthBits). Likewise, the height of the bitmap is 2 to the power of
   * heightBits, or (1 < < heightBits). The texture is shaded from it's
   * original color to black.
   */
  public ShadedTexture(byte[] buffer, int widthBits, int heightBits,
      IndexColorModel palette) {
    this(buffer, widthBits, heightBits, palette, Color.BLACK);
  }

  /**
   * Creates a new ShadedTexture from the specified 8-bit image buffer,
   * palette, and target shaded. The width of the bitmap is 2 to the power of
   * widthBits, or (1 < < widthBits). Likewise, the height of the bitmap is 2
   * to the power of heightBits, or (1 < < heightBits). The texture is shaded
   * from it's original color to the target shade.
   */
  public ShadedTexture(byte[] buffer, int widthBits, int heightBits,
      IndexColorModel palette, Color targetShade) {
    super(1 << widthBits, 1 << heightBits);
    this.buffer = buffer;
    this.widthBits = widthBits;
    this.heightBits = heightBits;
    this.widthMask = getWidth() - 1;
    this.heightMask = getHeight() - 1;
    this.buffer = buffer;
    this.palette = palette;
    defaultShadeLevel = MAX_LEVEL;

    makeShadeTable(targetShade);
  }

  /**
   * Creates the shade table for this ShadedTexture. Each entry in the palette
   * is shaded from the original color to the specified target color.
   */
  public void makeShadeTable(Color targetShade) {

    shadeTable = new short[NUM_SHADE_LEVELS * PALETTE_SIZE];

    for (int level = 0; level < NUM_SHADE_LEVELS; level++) {
      for (int i = 0; i < palette.getMapSize(); i++) {
        int red = calcColor(palette.getRed(i), targetShade.getRed(),
            level);
        int green = calcColor(palette.getGreen(i), targetShade
            .getGreen(), level);
        int blue = calcColor(palette.getBlue(i), targetShade.getBlue(),
            level);

        int index = level * PALETTE_SIZE + i;
        // RGB 5:6:5
        shadeTable[index] = (short) (((red >> 3) << 11)
            | ((green >> 2) << 5) | (blue >> 3));
      }
    }
  }

  private int calcColor(int palColor, int target, int level) {
    return (palColor - target) * (level + 1) / NUM_SHADE_LEVELS + target;
  }

  /**
   * Sets the default shade level that is used when getColor() is called.
   */
  public void setDefaultShadeLevel(int level) {
    defaultShadeLevel = level;
  }

  /**
   * Gets the default shade level that is used when getColor() is called.
   */
  public int getDefaultShadeLevel() {
    return defaultShadeLevel;
  }

  /**
   * Gets the 16-bit color of this Texture at the specified (x,y) location,
   * using the default shade level.
   */
  public short getColor(int x, int y) {
    return getColor(x, y, defaultShadeLevel);
  }

  /**
   * Gets the 16-bit color of this Texture at the specified (x,y) location,
   * using the specified shade level.
   */
  public short getColor(int x, int y, int shadeLevel) {
    return shadeTable[(shadeLevel << PALETTE_SIZE_BITS)
        | (0xff & buffer[(x & widthMask)
            | ((y & heightMask) << widthBits)])];
  }

  /**
   * Sets the current row for getColorCurrRow(). Pre-calculates the offset for
   * this row.
   */
  public void setCurrRow(int y) {
    currRow = (y & heightMask) << widthBits;
  }

  /**
   * Gets the color at the specified x location at the specified shade level.
   * The current row defined in setCurrRow is used.
   */
  public short getColorCurrRow(int x, int shadeLevel) {
    return shadeTable[(shadeLevel << PALETTE_SIZE_BITS)
        | (0xff & buffer[(x & widthMask) | currRow])];
  }

}

/**
 * The PowerOf2Texture class is a Texture with a width and height that are a
 * power of 2 (32, 128, etc.).
 */

final class PowerOf2Texture extends Texture {

  private short[] buffer;

  private int widthBits;

  private int widthMask;

  private int heightBits;

  private int heightMask;

  /**
   * Creates a new PowerOf2Texture with the specified buffer. The width of the
   * bitmap is 2 to the power of widthBits, or (1 < < widthBits). Likewise,
   * the height of the bitmap is 2 to the power of heightBits, or (1 < <
   * heightBits).
   */
  public PowerOf2Texture(short[] buffer, int widthBits, int heightBits) {
    super(1 << widthBits, 1 << heightBits);
    this.buffer = buffer;
    this.widthBits = widthBits;
    this.heightBits = heightBits;
    this.widthMask = getWidth() - 1;
    this.heightMask = getHeight() - 1;
  }

  /**
   * Gets the 16-bit color of the pixel at location (x,y) in the bitmap.
   */
  public short getColor(int x, int y) {
    return buffer[(x & widthMask) + ((y & heightMask) << widthBits)];
  }

}

/**
 * The FastTexturedPolygonRenderer is a PolygonRenderer that efficiently renders
 * Textures.
 */

class FastTexturedPolygonRenderer extends PolygonRenderer {

  public static final int SCALE_BITS = 12;

  public static final int SCALE = 1 << SCALE_BITS;

  public static final int INTERP_SIZE_BITS = 4;

  public static final int INTERP_SIZE = 1 << INTERP_SIZE_BITS;

  protected Vector3D a = new Vector3D();

  protected Vector3D b = new Vector3D();

  protected Vector3D c = new Vector3D();

  protected Vector3D viewPos = new Vector3D();

  protected BufferedImage doubleBuffer;

  protected short[] doubleBufferData;

  protected HashMap scanRenderers;

  public FastTexturedPolygonRenderer(Transform3D camera, ViewWindow viewWindow) {
    this(camera, viewWindow, true);
  }

  public FastTexturedPolygonRenderer(Transform3D camera,
      ViewWindow viewWindow, boolean clearViewEveryFrame) {
    super(camera, viewWindow, clearViewEveryFrame);
  }

  protected void init() {
    destPolygon = new TexturedPolygon3D();
    scanConverter = new ScanConverter(viewWindow);

    // create renders for each texture (HotSpot optimization)
    scanRenderers = new HashMap();
    scanRenderers.put(PowerOf2Texture.class, new PowerOf2TextureRenderer());
    scanRenderers.put(ShadedTexture.class, new ShadedTextureRenderer());
    scanRenderers.put(ShadedSurface.class, new ShadedSurfaceRenderer());
  }

  public void startFrame(Graphics2D g) {
    // initialize buffer
    if (doubleBuffer == null
        || doubleBuffer.getWidth() != viewWindow.getWidth()
        || doubleBuffer.getHeight() != viewWindow.getHeight()) {
      doubleBuffer = new BufferedImage(viewWindow.getWidth(), viewWindow
          .getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
      //doubleBuffer = g.getDeviceConfiguration().createCompatibleImage(
      //viewWindow.getWidth(), viewWindow.getHeight());

      DataBuffer dest = doubleBuffer.getRaster().getDataBuffer();
      doubleBufferData = ((DataBufferUShort) dest).getData();
    }
    // clear view
    if (clearViewEveryFrame) {
      for (int i = 0; i < doubleBufferData.length; i++) {
        doubleBufferData[i] = 0;
      }
    }
  }

  public void endFrame(Graphics2D g) {
    // draw the double buffer onto the screen
    g.drawImage(doubleBuffer, viewWindow.getLeftOffset(), viewWindow
        .getTopOffset(), null);
  }

  protected void drawCurrentPolygon(Graphics2D g) {
    if (!(sourcePolygon instanceof TexturedPolygon3D)) {
      // not a textured polygon - return
      return;
    }
    TexturedPolygon3D poly = (TexturedPolygon3D) destPolygon;
    Texture texture = poly.getTexture();
    ScanRenderer scanRenderer = (ScanRenderer) scanRenderers.get(texture
        .getClass());
    scanRenderer.setTexture(texture);
    Rectangle3D textureBounds = poly.getTextureBounds();

    a.setToCrossProduct(textureBounds.getDirectionV(), textureBounds
        .getOrigin());
    b.setToCrossProduct(textureBounds.getOrigin(), textureBounds
        .getDirectionU());
    c.setToCrossProduct(textureBounds.getDirectionU(), textureBounds
        .getDirectionV());

    int y = scanConverter.getTopBoundary();
    viewPos.y = viewWindow.convertFromScreenYToViewY(y);
    viewPos.z = -viewWindow.getDistance();

    while (y <= scanConverter.getBottomBoundary()) {
      ScanConverter.Scan scan = scanConverter.getScan(y);

      if (scan.isValid()) {
        viewPos.x = viewWindow.convertFromScreenXToViewX(scan.left);
        int offset = (y - viewWindow.getTopOffset())
            * viewWindow.getWidth()
            + (scan.left - viewWindow.getLeftOffset());

        scanRenderer.render(offset, scan.left, scan.right);
      }
      y++;
      viewPos.y--;
    }
  }

  /**
   * The ScanRenderer class is an abstract inner class of
   * FastTexturedPolygonRenderer that provides an interface for rendering a
   * horizontal scan line.
   */
  public abstract class ScanRenderer {

    protected Texture currentTexture;

    public void setTexture(Texture texture) {
      this.currentTexture = texture;
    }

    public abstract void render(int offset, int left, int right);

  }

  //================================================
  // FASTEST METHOD: no texture (for comparison)
  //================================================
  public class Method0 extends ScanRenderer {

    public void render(int offset, int left, int right) {
      for (int x = left; x <= right; x++) {
        doubleBufferData[offset++] = (short) 0x0007;
      }
    }
  }

  //================================================
  // METHOD 1: access pixel buffers directly
  // and use textures sizes that are a power of 2
  //================================================
  public class Method1 extends ScanRenderer {

    public void render(int offset, int left, int right) {
      for (int x = left; x <= right; x++) {
        int tx = (int) (a.getDotProduct(viewPos) / c
            .getDotProduct(viewPos));
        int ty = (int) (b.getDotProduct(viewPos) / c
            .getDotProduct(viewPos));
        doubleBufferData[offset++] = currentTexture.getColor(tx, ty);
        viewPos.x++;
      }
    }
  }

  //================================================
  // METHOD 2: avoid redundant calculations
  //================================================
  public class Method2 extends ScanRenderer {

    public void render(int offset, int left, int right) {
      float u = a.getDotProduct(viewPos);
      float v = b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = a.x;
      float dv = b.x;
      float dz = c.x;
      for (int x = left; x <= right; x++) {
        doubleBufferData[offset++] = currentTexture.getColor(
            (int) (u / z), (int) (v / z));
        u += du;
        v += dv;
        z += dz;
      }
    }
  }

  //================================================
  // METHOD 3: use ints instead of floats
  //================================================
  public class Method3 extends ScanRenderer {

    public void render(int offset, int left, int right) {
      int u = (int) (SCALE * a.getDotProduct(viewPos));
      int v = (int) (SCALE * b.getDotProduct(viewPos));
      int z = (int) (SCALE * c.getDotProduct(viewPos));
      int du = (int) (SCALE * a.x);
      int dv = (int) (SCALE * b.x);
      int dz = (int) (SCALE * c.x);
      for (int x = left; x <= right; x++) {
        doubleBufferData[offset++] = currentTexture.getColor(u / z, v
            / z);
        u += du;
        v += dv;
        z += dz;
      }
    }
  }

  //================================================
  // METHOD 4: reduce the number of divides
  // (interpolate every 16 pixels)
  // Also, apply a VM optimization by referring to
  // the texture's class rather than it's parent class.
  //================================================

  // the following three ScanRenderers are the same, but refer
  // to textures explicitly as either a PowerOf2Texture, a
  // ShadedTexture, or a ShadedSurface.
  // This allows HotSpot to do some inlining of the textures'
  // getColor() method, which significantly increases
  // performance.

  public class PowerOf2TextureRenderer extends ScanRenderer {

    public void render(int offset, int left, int right) {
      PowerOf2Texture texture = (PowerOf2Texture) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += interpSize;
        }

      }
    }
  }

  public class ShadedTextureRenderer extends ScanRenderer {

    public void render(int offset, int left, int right) {
      ShadedTexture texture = (ShadedTexture) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += interpSize;
        }

      }
    }
  }

  public class ShadedSurfaceRenderer extends ScanRenderer {

    public int checkBounds(int vScaled, int bounds) {
      int v = vScaled >> SCALE_BITS;
      if (v < 0) {
        vScaled = 0;
      } else if (v >= bounds) {
        vScaled = (bounds - 1) << SCALE_BITS;
      }
      return vScaled;
    }

    public void render(int offset, int left, int right) {
      ShadedSurface texture = (ShadedSurface) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);

          // make sure tx, ty, nextTx, and nextTy are
          // all within bounds
          tx = checkBounds(tx, texture.getWidth());
          ty = checkBounds(ty, texture.getHeight());
          nextTx = checkBounds(nextTx, texture.getWidth());
          nextTy = checkBounds(nextTy, texture.getHeight());

          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            doubleBufferData[offset++] = texture.getColor(
                tx >> SCALE_BITS, ty >> SCALE_BITS);
            tx += dtx;
            ty += dty;
          }
          x += interpSize;
        }
      }

    }
  }

}

/**
 * The ShadedSurfacePolygonRenderer is a PolygonRenderer that renders polygons
 * with ShadedSurfaces. It keeps track of built surfaces, and clears any
 * surfaces that weren't used in the last rendered frame to save memory.
 */

class ShadedSurfacePolygonRenderer extends FastTexturedPolygonRenderer {

  private List builtSurfaces = new LinkedList();

  public ShadedSurfacePolygonRenderer(Transform3D camera,
      ViewWindow viewWindow) {
    this(camera, viewWindow, true);
  }

  public ShadedSurfacePolygonRenderer(Transform3D camera,
      ViewWindow viewWindow, boolean eraseView) {
    super(camera, viewWindow, eraseView);
  }

  public void endFrame(Graphics2D g) {
    super.endFrame(g);

    // clear all built surfaces that weren't used this frame.
    Iterator i = builtSurfaces.iterator();
    while (i.hasNext()) {
      ShadedSurface surface = (ShadedSurface) i.next();
      if (surface.isDirty()) {
        surface.clearSurface();
        i.remove();
      } else {
        surface.setDirty(true);
      }
    }
  }

  protected void drawCurrentPolygon(Graphics2D g) {
    buildSurface();
    super.drawCurrentPolygon(g);
  }

  /**
   * Builds the surface of the polygon if it has a ShadedSurface that is
   * cleared.
   */
  protected void buildSurface() {
    // build surface, if needed
    if (sourcePolygon instanceof TexturedPolygon3D) {
      Texture texture = ((TexturedPolygon3D) sourcePolygon).getTexture();
      if (texture instanceof ShadedSurface) {
        ShadedSurface surface = (ShadedSurface) texture;
        if (surface.isCleared()) {
          surface.buildSurface();
          builtSurfaces.add(surface);
        }
        surface.setDirty(false);
      }
    }
  }

}

/**
 * The ZBufferedRenderer is a PolygonRenderer that renders polygons with a
 * Z-Buffer to ensure correct rendering (closer objects appear in front of
 * farther away objects).
 */

class ZBufferedRenderer extends ShadedSurfacePolygonRenderer implements
    GameObjectRenderer {
  /**
   * The minimum distance for z-buffering. Larger values give more accurate
   * calculations for further distances.
   */
  protected static final int MIN_DISTANCE = 12;

  protected TexturedPolygon3D temp;

  protected ZBuffer zBuffer;

  // used for calculating depth
  protected float w;

  public ZBufferedRenderer(Transform3D camera, ViewWindow viewWindow) {
    this(camera, viewWindow, true);
  }

  public ZBufferedRenderer(Transform3D camera, ViewWindow viewWindow,
      boolean eraseView) {
    super(camera, viewWindow, eraseView);
    temp = new TexturedPolygon3D();
  }

  protected void init() {
    destPolygon = new TexturedPolygon3D();
    scanConverter = new ScanConverter(viewWindow);

    // create renders for each texture (HotSpot optimization)
    scanRenderers = new HashMap();
    scanRenderers
        .put(PowerOf2Texture.class, new PowerOf2TextureZRenderer());
    scanRenderers.put(ShadedTexture.class, new ShadedTextureZRenderer());
    scanRenderers.put(ShadedSurface.class, new ShadedSurfaceZRenderer());
  }

  public void startFrame(Graphics2D g) {
    super.startFrame(g);
    // initialize depth buffer
    if (zBuffer == null || zBuffer.getWidth() != viewWindow.getWidth()
        || zBuffer.getHeight() != viewWindow.getHeight()) {
      zBuffer = new ZBuffer(viewWindow.getWidth(), viewWindow.getHeight());
    } else if (clearViewEveryFrame) {
      zBuffer.clear();
    }
  }

  public boolean draw(Graphics2D g, GameObject object) {
    return draw(g, object.getPolygonGroup());
  }

  public boolean draw(Graphics2D g, PolygonGroup group) {
    boolean visible = false;
    group.resetIterator();
    while (group.hasNext()) {
      group.nextPolygonTransformed(temp);
      visible |= draw(g, temp);
    }
    return visible;
  }

  protected void drawCurrentPolygon(Graphics2D g) {
    if (!(sourcePolygon instanceof TexturedPolygon3D)) {
      // not a textured polygon - return
      return;
    }
    buildSurface();
    TexturedPolygon3D poly = (TexturedPolygon3D) destPolygon;
    Texture texture = poly.getTexture();
    ScanRenderer scanRenderer = (ScanRenderer) scanRenderers.get(texture
        .getClass());
    scanRenderer.setTexture(texture);
    Rectangle3D textureBounds = poly.getTextureBounds();

    a.setToCrossProduct(textureBounds.getDirectionV(), textureBounds
        .getOrigin());
    b.setToCrossProduct(textureBounds.getOrigin(), textureBounds
        .getDirectionU());
    c.setToCrossProduct(textureBounds.getDirectionU(), textureBounds
        .getDirectionV());

    // w is used to compute depth at each pixel
    w = SCALE
        * MIN_DISTANCE
        * Short.MAX_VALUE
        / (viewWindow.getDistance() * c.getDotProduct(textureBounds
            .getOrigin()));

    int y = scanConverter.getTopBoundary();
    viewPos.y = viewWindow.convertFromScreenYToViewY(y);
    viewPos.z = -viewWindow.getDistance();

    while (y <= scanConverter.getBottomBoundary()) {
      ScanConverter.Scan scan = scanConverter.getScan(y);

      if (scan.isValid()) {
        viewPos.x = viewWindow.convertFromScreenXToViewX(scan.left);
        int offset = (y - viewWindow.getTopOffset())
            * viewWindow.getWidth()
            + (scan.left - viewWindow.getLeftOffset());

        scanRenderer.render(offset, scan.left, scan.right);
      }
      y++;
      viewPos.y--;
    }
  }

  // the following three ScanRenderers are the same, but refer
  // to textures explicitly as either a PowerOf2Texture, a
  // ShadedTexture, or a ShadedSurface.
  // This allows HotSpot to do some inlining of the textures'
  // getColor() method, which significantly increases
  // performance.

  public class PowerOf2TextureZRenderer extends ScanRenderer {

    public void render(int offset, int left, int right) {
      PowerOf2Texture texture = (PowerOf2Texture) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int depth = (int) (w * z);
      int dDepth = (int) (w * c.x);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset++] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += interpSize;

        }

      }
    }
  }

  public class ShadedTextureZRenderer extends ScanRenderer {

    public void render(int offset, int left, int right) {
      ShadedTexture texture = (ShadedTexture) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int depth = (int) (w * z);
      int dDepth = (int) (w * c.x);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += interpSize;
        }

      }
    }
  }

  public class ShadedSurfaceZRenderer extends ScanRenderer {

    public int checkBounds(int vScaled, int bounds) {
      int v = vScaled >> SCALE_BITS;
      if (v < 0) {
        vScaled = 0;
      } else if (v >= bounds) {
        vScaled = (bounds - 1) << SCALE_BITS;
      }
      return vScaled;
    }

    public void render(int offset, int left, int right) {
      ShadedSurface texture = (ShadedSurface) currentTexture;
      float u = SCALE * a.getDotProduct(viewPos);
      float v = SCALE * b.getDotProduct(viewPos);
      float z = c.getDotProduct(viewPos);
      float du = INTERP_SIZE * SCALE * a.x;
      float dv = INTERP_SIZE * SCALE * b.x;
      float dz = INTERP_SIZE * c.x;
      int nextTx = (int) (u / z);
      int nextTy = (int) (v / z);
      int depth = (int) (w * z);
      int dDepth = (int) (w * c.x);
      int x = left;
      while (x <= right) {
        int tx = nextTx;
        int ty = nextTy;
        int maxLength = right - x + 1;
        if (maxLength > INTERP_SIZE) {
          u += du;
          v += dv;
          z += dz;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);
          int dtx = (nextTx - tx) >> INTERP_SIZE_BITS;
          int dty = (nextTy - ty) >> INTERP_SIZE_BITS;
          int endOffset = offset + INTERP_SIZE;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += INTERP_SIZE;
        } else {
          // variable interpolation size
          int interpSize = maxLength;
          u += interpSize * SCALE * a.x;
          v += interpSize * SCALE * b.x;
          z += interpSize * c.x;
          nextTx = (int) (u / z);
          nextTy = (int) (v / z);

          // make sure tx, ty, nextTx, and nextTy are
          // all within bounds
          tx = checkBounds(tx, texture.getWidth());
          ty = checkBounds(ty, texture.getHeight());
          nextTx = checkBounds(nextTx, texture.getWidth());
          nextTy = checkBounds(nextTy, texture.getHeight());

          int dtx = (nextTx - tx) / interpSize;
          int dty = (nextTy - ty) / interpSize;
          int endOffset = offset + interpSize;
          while (offset < endOffset) {
            if (zBuffer.checkDepth(offset,
                (short) (depth >> SCALE_BITS))) {
              doubleBufferData[offset] = texture.getColor(
                  tx >> SCALE_BITS, ty >> SCALE_BITS);
            }
            offset++;
            tx += dtx;
            ty += dty;
            depth += dDepth;
          }
          x += interpSize;

        }

      }
    }
  }

}

/**
 * The ZBuffer class implements a z-buffer, or depth-buffer, that records the
 * depth of every pixel in a 3D view window. The value recorded for each pixel
 * is the inverse of the depth (1/z), so there is higher precision for close
 * objects and a lower precision for far-away objects (where high depth
 * precision is not as visually important).
 */

class ZBuffer {

  private short[] depthBuffer;

  private int width;

  private int height;

  /**
   * Creates a new z-buffer with the specified width and height.
   */
  public ZBuffer(int width, int height) {
    depthBuffer = new short[width * height];
    this.width = width;
    this.height = height;
    clear();
  }

  /**
   * Gets the width of this z-buffer.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of this z-buffer.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the array used for the depth buffer
   */
  public short[] getArray() {
    return depthBuffer;
  }

  /**
   * Clears the z-buffer. All depth values are set to 0.
   */
  public void clear() {
    for (int i = 0; i < depthBuffer.length; i++) {
      depthBuffer[i] = 0;
    }
  }

  /**
   * Sets the depth of the pixel at at specified offset, overwriting its
   * current depth.
   */
  public void setDepth(int offset, short depth) {
    depthBuffer[offset] = depth;
  }

  /**
   * Checks the depth at the specified offset, and if the specified depth is
   * lower (is greater than or equal to the current depth at the specified
   * offset), then the depth is set and this method returns true. Otherwise,
   * no action occurs and this method returns false.
   */
  public boolean checkDepth(int offset, short depth) {
    if (depth >= depthBuffer[offset]) {
      depthBuffer[offset] = depth;
      return true;
    } else {
      return false;
    }
  }

}
/**
 * The BSPRenderer class is a renderer capable of drawing polygons in a BSP tree
 * and any polygon objects in the scene. When drawing BSP polygons, the
 * BSPRenderer writes the BSP polygon depth to a z-buffer. Polygon objects use
 * the z-buffer to determine their visibility within the scene on a per-pixel
 * basis.
 */

class BSPRenderer extends ZBufferedRenderer implements BSPTreeTraverseListener {

  /**
   * How many polygons to draw before checking if the view is filled.
   */
  private static final int FILLED_CHECK = 3;

  protected HashMap bspScanRenderers;

  protected BSPTreeTraverser traverser;

  protected Graphics2D currentGraphics2D;

  protected boolean viewNotFilledFirstTime;

  protected int polygonCount;

  /**
   * Creates a new BSP renderer with the specified camera object and view
   * window.
   */
  public BSPRenderer(Transform3D camera, ViewWindow viewWindow) {
    super(camera, viewWindow, false);
    viewNotFilledFirstTime = true;
    traverser = new BSPTreeTraverser(this);
  }

  /**
   * Sets the GamebjectManager. The BSP traverser sets the visibily of the
   * objects.
   */
  public void setGameObjectManager(GameObjectManager gameObjectManager) {
    traverser.setGameObjectManager(gameObjectManager);
  }

  protected void init() {
    destPolygon = new TexturedPolygon3D();
    scanConverter = new SortedScanConverter(viewWindow);

    // create renderers for each texture (HotSpot optimization)
    scanRenderers = new HashMap();
    scanRenderers
        .put(PowerOf2Texture.class, new PowerOf2TextureZRenderer());
    scanRenderers.put(ShadedTexture.class, new ShadedTextureZRenderer());
    scanRenderers.put(ShadedSurface.class, new ShadedSurfaceZRenderer());

    // same thing, for bsp tree polygons
    bspScanRenderers = new HashMap();
    bspScanRenderers.put(PowerOf2Texture.class,
        new PowerOf2TextureRenderer());
    bspScanRenderers.put(ShadedTexture.class, new ShadedTextureRenderer());
    bspScanRenderers.put(ShadedSurface.class, new ShadedSurfaceRenderer());
  }

  public void startFrame(Graphics2D g) {
    super.startFrame(g);
    ((SortedScanConverter) scanConverter).clear();
    polygonCount = 0;
  }

  public void endFrame(Graphics2D g) {
    super.endFrame(g);
    if (!((SortedScanConverter) scanConverter).isFilled()) {
      g.drawString("View not completely filled", 5, viewWindow
          .getTopOffset()
          + viewWindow.getHeight() - 5);
      if (viewNotFilledFirstTime) {
        viewNotFilledFirstTime = false;
        // print message to console in case user missed it
        System.out.println("View not completely filled.");
      }
      // clear the background next time
      clearViewEveryFrame = true;
    } else {
      clearViewEveryFrame = false;
    }
  }

  /**
   * Draws the visible polygons in a BSP tree based on the camera location.
   * The polygons are drawn front-to-back.
   */
  public void draw(Graphics2D g, BSPTree tree) {
    ((SortedScanConverter) scanConverter).setSortedMode(true);
    currentGraphics2D = g;
    traverser.traverse(tree, camera.getLocation());
    ((SortedScanConverter) scanConverter).setSortedMode(false);
  }

  // from the BSPTreeTraverseListener interface
  public boolean visitPolygon(BSPPolygon poly, boolean isBack) {
    SortedScanConverter scanConverter = (SortedScanConverter) this.scanConverter;

    draw(currentGraphics2D, poly);

    // check if view is filled every three polygons
    polygonCount++;
    if (polygonCount == FILLED_CHECK) {
      polygonCount = 0;
      return !((SortedScanConverter) scanConverter).isFilled();
    }
    return true;
  }

  protected void drawCurrentPolygon(Graphics2D g) {
    if (!(sourcePolygon instanceof BSPPolygon)) {
      super.drawCurrentPolygon(g);
      return;
    }
    buildSurface();
    SortedScanConverter scanConverter = (SortedScanConverter) this.scanConverter;
    TexturedPolygon3D poly = (TexturedPolygon3D) destPolygon;
    Texture texture = poly.getTexture();
    ScanRenderer scanRenderer = (ScanRenderer) bspScanRenderers.get(texture
        .getClass());
    scanRenderer.setTexture(texture);
    Rectangle3D textureBounds = poly.getTextureBounds();

    a.setToCrossProduct(textureBounds.getDirectionV(), textureBounds
        .getOrigin());
    b.setToCrossProduct(textureBounds.getOrigin(), textureBounds
        .getDirectionU());
    c.setToCrossProduct(textureBounds.getDirectionU(), textureBounds
        .getDirectionV());

    // w is used to compute depth at each pixel
    w = SCALE
        * MIN_DISTANCE
        * Short.MAX_VALUE
        / (viewWindow.getDistance() * c.getDotProduct(textureBounds
            .getOrigin()));

    int y = scanConverter.getTopBoundary();
    viewPos.y = viewWindow.convertFromScreenYToViewY(y);
    viewPos.z = -viewWindow.getDistance();

    while (y <= scanConverter.getBottomBoundary()) {
      for (int i = 0; i < scanConverter.getNumScans(y); i++) {
        ScanConverter.Scan scan = scanConverter.getScan(y, i);

        if (scan.isValid()) {
          viewPos.x = viewWindow.convertFromScreenXToViewX(scan.left);
          int offset = (y - viewWindow.getTopOffset())
              * viewWindow.getWidth()
              + (scan.left - viewWindow.getLeftOffset());

          scanRenderer.render(offset, scan.left, scan.right);
          setScanDepth(offset, scan.right - scan.left + 1);
        }
      }
      y++;
      viewPos.y--;
    }
  }

  /**
   * Sets the z-depth for the current polygon scan.
   */
  private void setScanDepth(int offset, int width) {
    float z = c.getDotProduct(viewPos);
    float dz = c.x;
    int depth = (int) (w * z);
    int dDepth = (int) (w * dz);
    short[] depthBuffer = zBuffer.getArray();
    int endOffset = offset + width;

    // depth will be constant for many floors and ceilings
    if (dDepth == 0) {
      short d = (short) (depth >> SCALE_BITS);
      while (offset < endOffset) {
        depthBuffer[offset++] = d;
      }
    } else {
      while (offset < endOffset) {
        depthBuffer[offset++] = (short) (depth >> SCALE_BITS);
        depth += dDepth;
      }
    }
  }

}
/**
 * A ScanConverter used to draw sorted polygons from front-to-back with no
 * overdraw. Polygons are added and clipped to a list of what's in the view
 * window. Call clear() before drawing every frame.
 */

class SortedScanConverter extends ScanConverter {

  protected static final int DEFAULT_SCANLIST_CAPACITY = 8;

  private SortedScanList[] viewScans;

  private SortedScanList[] polygonScans;

  private boolean sortedMode;

  /**
   * Creates a new SortedScanConverter for the specified ViewWindow. The
   * ViewWindow's properties can change in between scan conversions. By
   * default, sorted mode is off, but can be turned on by calling
   * setSortedMode().
   */
  public SortedScanConverter(ViewWindow view) {
    super(view);
    sortedMode = false;
  }

  /**
   * Clears the current view scan. Call this method every frame.
   */
  public void clear() {
    if (viewScans != null) {
      for (int y = 0; y < viewScans.length; y++) {
        viewScans[y].clear();
      }
    }
  }

  /**
   * Sets sorted mode, so this scan converter can assume the polygons are
   * drawn front-to-back, and should be clipped against polygons already
   * scanned for this view.
   */
  public void setSortedMode(boolean b) {
    sortedMode = b;
  }

  /**
   * Gets the nth scan for the specified row.
   */
  public Scan getScan(int y, int index) {
    return polygonScans[y].getScan(index);
  }

  /**
   * Gets the number of scans for the specified row.
   */
  public int getNumScans(int y) {
    return polygonScans[y].getNumScans();
  }

  /**
   * Checks if the view is filled.
   */
  public boolean isFilled() {
    if (viewScans == null) {
      return false;
    }

    int left = view.getLeftOffset();
    int right = left + view.getWidth() - 1;
    for (int y = view.getTopOffset(); y < viewScans.length; y++) {
      if (!viewScans[y].equals(left, right)) {
        return false;
      }
    }
    return true;
  }

  protected void ensureCapacity() {
    super.ensureCapacity();
    int height = view.getTopOffset() + view.getHeight();
    int oldHeight = (viewScans == null) ? 0 : viewScans.length;
    if (height != oldHeight) {
      SortedScanList[] newViewScans = new SortedScanList[height];
      SortedScanList[] newPolygonScans = new SortedScanList[height];
      if (oldHeight != 0) {
        System.arraycopy(viewScans, 0, newViewScans, 0, Math.min(
            height, oldHeight));
        System.arraycopy(polygonScans, 0, newPolygonScans, 0, Math.min(
            height, oldHeight));
      }
      viewScans = newViewScans;
      polygonScans = newPolygonScans;
      for (int i = oldHeight; i < height; i++) {
        viewScans[i] = new SortedScanList();
        polygonScans[i] = new SortedScanList();
      }
    }
  }

  /**
   * Scan-converts a polygon, and if sortedMode is on, adds and clips it to a
   * list of what's in the view window.
   */
  public boolean convert(Polygon3D polygon) {
    boolean visible = super.convert(polygon);
    if (!sortedMode || !visible) {
      return visible;
    }

    // clip the scan to what's already in the view
    visible = false;
    for (int y = getTopBoundary(); y <= getBottomBoundary(); y++) {
      Scan scan = getScan(y);
      SortedScanList diff = polygonScans[y];
      diff.clear();
      if (scan.isValid()) {
        viewScans[y].add(scan.left, scan.right, diff);
        visible |= (polygonScans[y].getNumScans() > 0);
      }
    }

    return visible;

  }

  /**
   * The SortedScanList class represents a series of scans for a row. New
   * scans can be added and clipped to what's visible in the row.
   */
  private static class SortedScanList {

    private int length;

    private Scan[] scans;

    /**
     * Creates a new SortedScanList with the default capacity (number of
     * scans per row).
     */
    public SortedScanList() {
      this(DEFAULT_SCANLIST_CAPACITY);
    }

    /**
     * Creates a new SortedScanList with the specified capacity (number of
     * scans per row).
     */
    public SortedScanList(int capacity) {
      scans = new Scan[capacity];
      for (int i = 0; i < capacity; i++) {
        scans[i] = new Scan();
      }
      length = 0;
    }

    /**
     * Clears this list of scans.
     */
    public void clear() {
      length = 0;
    }

    /**
     * Clears the number of scans in this list.
     */
    public int getNumScans() {
      return length;
    }

    /**
     * Gets the nth scan in this list.
     */
    public Scan getScan(int index) {
      return scans[index];
    }

    /**
     * Checks if this scan list has only one scan and that scan is equal to
     * the specified left and right values.
     */
    public boolean equals(int left, int right) {
      return (length == 1 && scans[0].equals(left, right));
    }

    /**
     * Add and clip the scan to this row, putting what is visible (the
     * difference) in the specified SortedScanList.
     */
    public void add(int left, int right, SortedScanList diff) {
      for (int i = 0; i < length && left <= right; i++) {
        Scan scan = scans[i];
        int maxRight = scan.left - 1;
        if (left <= maxRight) {
          if (right < maxRight) {
            diff.add(left, right);
            insert(left, right, i);
            return;
          } else {
            diff.add(left, maxRight);
            scan.left = left;
            left = scan.right + 1;
            if (merge(i)) {
              i--;
            }
          }
        } else if (left <= scan.right) {
          left = scan.right + 1;
        }
      }
      if (left <= right) {
        insert(left, right, length);
        diff.add(left, right);
      }

    }

    // add() helper methods

    private void growCapacity() {
      int capacity = scans.length;
      int newCapacity = capacity * 2;
      Scan[] newScans = new Scan[newCapacity];
      System.arraycopy(scans, 0, newScans, 0, capacity);
      for (int i = length; i < newCapacity; i++) {
        newScans[i] = new Scan();
      }
      scans = newScans;
    }

    private void add(int left, int right) {
      if (length == scans.length) {
        growCapacity();
      }
      scans[length].setTo(left, right);
      length++;
    }

    private void insert(int left, int right, int index) {
      if (index > 0) {
        Scan prevScan = scans[index - 1];
        if (prevScan.right == left - 1) {
          prevScan.right = right;
          return;
        }
      }

      if (length == scans.length) {
        growCapacity();
      }
      Scan last = scans[length];
      last.setTo(left, right);
      for (int i = length; i > index; i--) {
        scans[i] = scans[i - 1];
      }
      scans[index] = last;
      length++;
    }

    private void remove(int index) {
      Scan removed = scans[index];
      for (int i = index; i < length - 1; i++) {
        scans[i] = scans[i + 1];
      }
      scans[length - 1] = removed;
      length--;
    }

    private boolean merge(int index) {
      if (index > 0) {
        Scan prevScan = scans[index - 1];
        Scan thisScan = scans[index];
        if (prevScan.right == thisScan.left - 1) {
          prevScan.right = thisScan.right;
          remove(index);
          return true;
        }
      }
      return false;
    }

  }

}

/**
 * The Bot game object is a small static bot with a turret that turns to face
 * the player.
*/

class Bot extends GameObject {

  private static final float TURN_SPEED = .0005f;

  private static final long DECISION_TIME = 2000;

  protected MovingTransform3D mainTransform;

  protected MovingTransform3D turretTransform;

  protected long timeUntilDecision;

  protected Vector3D lastPlayerLocation;

  public Bot(PolygonGroup polygonGroup) {
    super(polygonGroup);
    mainTransform = polygonGroup.getTransform();
    PolygonGroup turret = polygonGroup.getGroup("turret");
    if (turret != null) {
      turretTransform = turret.getTransform();
    } else {
      System.out.println("No turret defined!");
    }
    lastPlayerLocation = new Vector3D();
  }

  public void notifyVisible(boolean visible) {
    if (!isDestroyed()) {
      if (visible) {
        setState(STATE_ACTIVE);
      } else {
        setState(STATE_IDLE);
      }
    }
  }

  public void update(GameObject player, long elapsedTime) {
    if (turretTransform == null || isIdle()) {
      return;
    }

    Vector3D playerLocation = player.getLocation();
    if (playerLocation.equals(lastPlayerLocation)) {
      timeUntilDecision = DECISION_TIME;
    } else {
      timeUntilDecision -= elapsedTime;
      if (timeUntilDecision <= 0 || !turretTransform.isTurningY()) {
        float x = player.getX() - getX();
        float z = player.getZ() - getZ();
        turretTransform.turnYTo(x, z, -mainTransform.getAngleY(),
            TURN_SPEED);
        lastPlayerLocation.setTo(playerLocation);
        timeUntilDecision = DECISION_TIME;
      }
    }
    super.update(player, elapsedTime);
  }
}
