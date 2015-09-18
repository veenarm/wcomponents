package com.github.bordertech.wcomponents.container;

import com.github.bordertech.wcomponents.WComponent;

/**
 * This interface is used to integrate a container (e.g. WServlet, WPortlet) with a ContainerHelper (e.g.
 * WPortletHelper).
 *
 * @author Martin Shevchenko
 * @since 1.0.0
 * @deprecated no longer used
 */
@Deprecated
public interface WComponentContainer {

	/**
	 * <p>
	 * Subclasses may override this method.</p>
	 *
	 * <p>
	 * This method will be called internally once per request/response cycle.</p>
	 *
	 * <p>
	 * It is up to the subclass to determine the life cycle of the returned component. Should the same instance be
	 * returned and shared by all sessions, or should there be a new instance per session.</p>
	 *
	 * <p>
	 * A good way to always return the same instance is to use the UIRegistry. E.g.
	 * <pre>
	 *   return UIRegistry.getInstance().getUI(KitchenSink.class.getName());
	 * </pre>
	 * </p>
	 *
	 * @param nativeRequest the native request being responded to, for example an HttpServletRequest or PortletRequest.
	 *
	 * @return the top-level WComponent which defines the UI.
	 */
	WComponent getUI(Object nativeRequest);

	/**
	 * The interceptor component returned here is used to wrap the html fragment generated by the UI component. Portlets
	 * and Servlets need to perform different wrapping of the UI component.
	 *
	 * @param nativeRequest the native request being responded to, for example an HttpServletRequest or PortletRequest.
	 *
	 * @return the top-level interceptor in the chain.
	 */
	InterceptorComponent createInterceptorChain(Object nativeRequest);
}
