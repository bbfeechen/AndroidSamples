package com.sony.imaging.app.srctrl.webapi;

import com.sony.scalar.webapi.servlet.MethodInvokerBase;

public class SRCtrlServlet extends MethodInvokerBase {

    private static final long serialVersionUID = 1L;

    @Override
    public final void init()
            throws javax.servlet.ServletException {
        addVersion("1.0", new com.sony.imaging.app.srctrl.webapi.v1_0.SRCtrlServlet());
    }
}
