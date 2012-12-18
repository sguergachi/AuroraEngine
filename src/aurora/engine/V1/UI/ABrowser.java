package aurora.engine.V1.UI;

import chrriis.dj.nativeswing.NSOption;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class ABrowser extends JWebBrowser {

    private static final long serialVersionUID = 1L;

    public ABrowser(NSOption... options) {

        super(options);
        this.setBarsVisible(false);

    }
}