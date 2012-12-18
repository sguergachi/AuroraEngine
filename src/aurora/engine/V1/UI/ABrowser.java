package aurora.engine.V1.UI;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class ABrowser extends JWebBrowser{
	private JWebBrowser browser;
	
	public ABrowser(){
		browser = new JWebBrowser();
		browser.setBarsVisible(false);
	}
}