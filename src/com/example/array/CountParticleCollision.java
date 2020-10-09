package com.example.array;

/**
 * Given the array. array index is the position of the particle and array value is speed of the paricle
 * for e.g. [3,2,5] means 
 * Particle at 0m position has 3m/s speed
 * Particle at 1m position has 2m/s speed
 * Particle at 2m position has 5m/s speed
 * Give the position of the particle, count the total number of collision of that particle with others
 * (Hint: Particle 0 and 1 will collide at 3m after 1 second)
 */
public class CountParticleCollision {

	public static void main(String[] args) {
		System.out.println(countParticleCollision(new int[] {2, 1}, 0));
		System.out.println(countParticleCollision(new int[] {6, 6, 1, 6, 3, 4, 6, 8}, 1));
	}
	
	private static int countParticleCollision(int[] speed, int pos) {
		int count = 0;
		 for(int i=0; i<speed.length; i++) {
			 if(i != pos) {
				 int particle = speed[pos] + pos;
				 int otherParticle = speed[i] + i;
				 while(particle <= otherParticle) {
					 if(particle == otherParticle) {
						 count++;
					 }
					 particle = particle + speed[pos];
					 otherParticle = otherParticle + speed[i];
				 }
			 }		 
		 }
		 return count;
	}
}
