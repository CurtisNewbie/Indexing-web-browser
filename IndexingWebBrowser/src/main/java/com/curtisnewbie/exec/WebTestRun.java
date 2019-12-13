package com.curtisnewbie.exec;

import com.curtisnewbie.controller.WebBrowserController;
import com.curtisnewbie.view.WebBrowserView;

/**
 * This class is used to instantiate a new object of WebBrowserView as the view
 * in MVC architecture and a new object of WebBrowserController as the
 * controller in the MVC.
 * 
 * @author 180139796
 *
 */
public class WebTestRun {

	public static void main(String[] args) {
		WebBrowserView view = new WebBrowserView();
		WebBrowserController controller = new WebBrowserController(view);
	}
}
