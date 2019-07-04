package com.help4j.advance.pattern;

public class DecoratorPattern {

	public static void main(String[] args) {
		Window basicWindow = new BasicWindow();
		basicWindow.draw();
		System.out.println();
		Window borderWindow = new BorderDecorator(basicWindow);
		borderWindow.draw();
		System.out.println();
		Window borderWindowScrollable = new ScrollDecorator(borderWindow);
		borderWindowScrollable.draw();
	}
}

interface Window {

	public void draw();

}

class BasicWindow implements Window {
	@Override
	public void draw() {
		System.out.print("Basic Window");
	}
}

class WindowDecorator implements Window {

	protected Window window;

	public WindowDecorator(Window window) {
		this.window = window;
	}

	@Override
	public void draw() {
		window.draw();
	}

}

class BorderDecorator extends WindowDecorator {

	public BorderDecorator(Window window) {
		super(window);
	}

	@Override
	public void draw() {
		window.draw();
		addBorder();
	}

	public void addBorder() {
		System.out.print(" + Border");
	}
}

class ScrollDecorator extends WindowDecorator {

	public ScrollDecorator(Window window) {
		super(window);
	}

	@Override
	public void draw() {
		window.draw();
		addScroll();
	}

	public void addScroll() {
		System.out.print(" + Scroll");
	}
}