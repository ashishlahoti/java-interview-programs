package com.example.designpattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecoratorPattern {

	@Test
	public void testDecorators() {
		Window basicWindow = new BasicWindow();
		assertEquals(basicWindow.draw(), "Basic Window");
		
		Window borderWindow = new BorderDecorator(basicWindow);
		assertEquals(borderWindow.draw(), "Basic Window with Border");

		Window borderWindowScrollable = new ScrollDecorator(borderWindow);
		assertEquals(borderWindowScrollable.draw(), "Basic Window with Border and Scroll Bar");
	}
}

interface Window {

	public String draw();

}

class BasicWindow implements Window {
	@Override
	public String draw() {
		return "Basic Window";
	}
}

class WindowDecorator implements Window {

	protected Window window;

	public WindowDecorator(Window window) {
		this.window = window;
	}

	@Override
	public String draw() {
		return window.draw();
	}

}

class BorderDecorator extends WindowDecorator {

	public BorderDecorator(Window window) {
		super(window);
	}

	@Override
	public String draw() {
		return window.draw() + addBorder();
	}

	public String addBorder() {
		return " with Border";
	}
}

class ScrollDecorator extends WindowDecorator {

	public ScrollDecorator(Window window) {
		super(window);
	}

	@Override
	public String draw() {
		return window.draw() + addScroll();
	}

	public String addScroll() {
		return " and Scroll Bar";
	}
}