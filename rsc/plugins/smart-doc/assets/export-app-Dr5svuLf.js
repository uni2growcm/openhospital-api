import { c as clientExports, A as App } from './styles-hjhuduQt.js';
import { a as React3, _ as __mf_14, b as __mf_2, c as __mf_13 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './tooltip-Cuc2eMnW.js';
import './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import './preload-helper-BgB2ycR-.js';

var define_process_env_default = {};
const BROWSER_LOG_KEY = "FEDERATION_DEBUG";
const isBrowserEnvValue = true ;
function isBrowserEnv() {
  return isBrowserEnvValue;
}
function isBrowserDebug() {
  try {
    if (isBrowserEnv() && window.localStorage) return Boolean(localStorage.getItem(BROWSER_LOG_KEY));
  } catch (error) {
    return false;
  }
  return false;
}
function isDebugMode() {
  if (typeof process !== "undefined" && define_process_env_default && define_process_env_default["FEDERATION_DEBUG"]) return Boolean(define_process_env_default["FEDERATION_DEBUG"]);
  if (typeof FEDERATION_DEBUG !== "undefined" && Boolean(FEDERATION_DEBUG)) return true;
  return isBrowserDebug();
}
const PREFIX = "[ Module Federation ]";
const DEFAULT_DELEGATE = console;
const LOGGER_STACK_SKIP_TOKENS = [
  "logger.ts",
  "logger.js",
  "captureStackTrace",
  "Logger.emit",
  "Logger.log",
  "Logger.info",
  "Logger.warn",
  "Logger.error",
  "Logger.debug"
];
function captureStackTrace() {
  try {
    const stack = (/* @__PURE__ */ new Error()).stack;
    if (!stack) return;
    const [, ...rawLines] = stack.split("\n");
    const filtered = rawLines.filter((line) => !LOGGER_STACK_SKIP_TOKENS.some((token) => line.includes(token)));
    if (!filtered.length) return;
    return `Stack trace:
${filtered.slice(0, 5).join("\n")}`;
  } catch {
    return;
  }
}
var Logger = class {
  constructor(prefix, delegate = DEFAULT_DELEGATE) {
    this.prefix = prefix;
    this.delegate = delegate ?? DEFAULT_DELEGATE;
  }
  setPrefix(prefix) {
    this.prefix = prefix;
  }
  setDelegate(delegate) {
    this.delegate = delegate ?? DEFAULT_DELEGATE;
  }
  emit(method, args) {
    const delegate = this.delegate;
    const stackTrace = isDebugMode() ? captureStackTrace() : void 0;
    const enrichedArgs = stackTrace ? [...args, stackTrace] : args;
    const order = (() => {
      switch (method) {
        case "log":
          return ["log", "info"];
        case "info":
          return ["info", "log"];
        case "warn":
          return [
            "warn",
            "info",
            "log"
          ];
        case "error":
          return [
            "error",
            "warn",
            "log"
          ];
        default:
          return ["debug", "log"];
      }
    })();
    for (const candidate of order) {
      const handler = delegate[candidate];
      if (typeof handler === "function") {
        handler.call(delegate, this.prefix, ...enrichedArgs);
        return;
      }
    }
    for (const candidate of order) {
      const handler = DEFAULT_DELEGATE[candidate];
      if (typeof handler === "function") {
        handler.call(DEFAULT_DELEGATE, this.prefix, ...enrichedArgs);
        return;
      }
    }
  }
  log(...args) {
    this.emit("log", args);
  }
  warn(...args) {
    this.emit("warn", args);
  }
  error(...args) {
    this.emit("error", args);
  }
  success(...args) {
    this.emit("info", args);
  }
  info(...args) {
    this.emit("info", args);
  }
  ready(...args) {
    this.emit("info", args);
  }
  debug(...args) {
    if (isDebugMode()) this.emit("debug", args);
  }
};
function createLogger(prefix) {
  return new Logger(prefix);
}
function createInfrastructureLogger(prefix) {
  const infrastructureLogger = new Logger(prefix);
  Object.defineProperty(infrastructureLogger, "__mf_infrastructure_logger__", {
    value: true,
    enumerable: false,
    configurable: false
  });
  return infrastructureLogger;
}
createInfrastructureLogger(PREFIX);

const RouterContext = React3.createContext(null);
const LoggerInstance = createLogger(
  "[ Module Federation Bridge React ]"
);

const federationRuntime = { instance: null };

const ErrorBoundaryContext = __mf_13(null);
const initialState = {
  didCatch: false,
  error: null
};
class ErrorBoundary extends __mf_2 {
  constructor(props) {
    super(props);
    this.resetErrorBoundary = this.resetErrorBoundary.bind(this);
    this.state = initialState;
  }
  static getDerivedStateFromError(error) {
    return {
      didCatch: true,
      error
    };
  }
  resetErrorBoundary() {
    const {
      error
    } = this.state;
    if (error !== null) {
      var _this$props$onReset, _this$props;
      for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }
      (_this$props$onReset = (_this$props = this.props).onReset) === null || _this$props$onReset === void 0 ? void 0 : _this$props$onReset.call(_this$props, {
        args,
        reason: "imperative-api"
      });
      this.setState(initialState);
    }
  }
  componentDidCatch(error, info) {
    var _this$props$onError, _this$props2;
    (_this$props$onError = (_this$props2 = this.props).onError) === null || _this$props$onError === void 0 ? void 0 : _this$props$onError.call(_this$props2, error, info);
  }
  componentDidUpdate(prevProps, prevState) {
    const {
      didCatch
    } = this.state;
    const {
      resetKeys
    } = this.props;
    if (didCatch && prevState.error !== null && hasArrayChanged(prevProps.resetKeys, resetKeys)) {
      var _this$props$onReset2, _this$props3;
      (_this$props$onReset2 = (_this$props3 = this.props).onReset) === null || _this$props$onReset2 === void 0 ? void 0 : _this$props$onReset2.call(_this$props3, {
        next: resetKeys,
        prev: prevProps.resetKeys,
        reason: "keys"
      });
      this.setState(initialState);
    }
  }
  render() {
    const {
      children,
      fallbackRender,
      FallbackComponent,
      fallback
    } = this.props;
    const {
      didCatch,
      error
    } = this.state;
    let childToRender = children;
    if (didCatch) {
      const props = {
        error,
        resetErrorBoundary: this.resetErrorBoundary
      };
      if (typeof fallbackRender === "function") {
        childToRender = fallbackRender(props);
      } else if (FallbackComponent) {
        childToRender = __mf_14(FallbackComponent, props);
      } else if (fallback !== void 0) {
        childToRender = fallback;
      } else {
        throw error;
      }
    }
    return __mf_14(ErrorBoundaryContext.Provider, {
      value: {
        didCatch,
        error,
        resetErrorBoundary: this.resetErrorBoundary
      }
    }, childToRender);
  }
}
function hasArrayChanged() {
  let a = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : [];
  let b = arguments.length > 1 && arguments[1] !== void 0 ? arguments[1] : [];
  return a.length !== b.length || a.some((item, index) => !Object.is(item, b[index]));
}
function createBaseBridgeComponent({
  createRoot,
  defaultRootOptions,
  ...bridgeInfo
}) {
  return () => {
    const rootMap = /* @__PURE__ */ new Map();
    const instance = federationRuntime.instance;
    LoggerInstance.debug(
      `createBridgeComponent instance from props >>>`,
      instance
    );
    const RawComponent = (info) => {
      const { appInfo, propsInfo, ...restProps } = info;
      const { moduleName, memoryRoute, basename = "/" } = appInfo;
      return /* @__PURE__ */ __mf_14(RouterContext.Provider, { value: { moduleName, basename, memoryRoute } }, /* @__PURE__ */ __mf_14(
        bridgeInfo.rootComponent,
        {
          ...propsInfo,
          basename,
          ...restProps
        }
      ));
    };
    const DefaultFallback = ({ error }) => /* @__PURE__ */ __mf_14("div", { role: "alert" }, /* @__PURE__ */ __mf_14("p", null, "Something went wrong:"), /* @__PURE__ */ __mf_14("pre", { style: { color: "red" } }, error.message));
    const ErrorBoundaryComponent = ErrorBoundary;
    const BridgeWrapper = ({
      basename,
      moduleName,
      memoryRoute,
      propsInfo,
      fallback
    }) => /* @__PURE__ */ __mf_14(ErrorBoundaryComponent, { FallbackComponent: fallback || DefaultFallback }, /* @__PURE__ */ __mf_14(
      RawComponent,
      {
        appInfo: {
          moduleName,
          basename,
          memoryRoute
        },
        propsInfo
      }
    ));
    return {
      async render(info) {
        var _a, _b, _c, _d, _e, _f;
        LoggerInstance.debug(`createBridgeComponent render Info`, info);
        const {
          moduleName,
          dom,
          basename,
          memoryRoute,
          fallback,
          rootOptions,
          ...propsInfo
        } = info;
        const mergedRootOptions = {
          ...defaultRootOptions,
          ...rootOptions
        };
        const beforeBridgeRenderRes = ((_c = (_b = (_a = void 0 ) == null ? void 0 : _a.lifecycle) == null ? void 0 : _b.beforeBridgeRender) == null ? void 0 : _c.emit(info)) || {};
        const rootComponentWithErrorBoundary = /* @__PURE__ */ __mf_14(
          BridgeWrapper,
          {
            basename,
            moduleName,
            memoryRoute,
            fallback,
            propsInfo: {
              ...propsInfo,
              basename,
              ...beforeBridgeRenderRes == null ? void 0 : beforeBridgeRenderRes.extraProps
            }
          }
        );
        if (bridgeInfo.render) {
          await Promise.resolve(
            bridgeInfo.render(rootComponentWithErrorBoundary, dom)
          ).then((root) => rootMap.set(dom, root));
        } else {
          let root = rootMap.get(dom);
          if (!root && createRoot) {
            root = createRoot(dom, mergedRootOptions);
            rootMap.set(dom, root);
          }
          if (root && "render" in root) {
            root.render(rootComponentWithErrorBoundary);
          }
        }
        ((_f = (_e = (_d = void 0 ) == null ? void 0 : _d.lifecycle) == null ? void 0 : _e.afterBridgeRender) == null ? void 0 : _f.emit(info)) || {};
      },
      destroy(info) {
        var _a, _b, _c;
        const { dom } = info;
        LoggerInstance.debug(`createBridgeComponent destroy Info`, info);
        const root = rootMap.get(dom);
        if (root) {
          if ("unmount" in root) {
            root.unmount();
          } else {
            LoggerInstance.warn("Root does not have unmount method");
          }
          rootMap.delete(dom);
        }
        (_c = (_b = (_a = void 0 ) == null ? void 0 : _a.lifecycle) == null ? void 0 : _b.afterBridgeDestroy) == null ? void 0 : _c.emit(info);
      }
    };
  };
}

function createReact19Root(container, options) {
  return clientExports.createRoot(container, options);
}
function createBridgeComponent(bridgeInfo) {
  const fullBridgeInfo = {
    createRoot: createReact19Root,
    ...bridgeInfo
  };
  return createBaseBridgeComponent(fullBridgeInfo);
}

const provider = createBridgeComponent({
  rootComponent: App,
  defaultRootOptions: {
    identifierPrefix: "smart-doc-app-",
    onRecoverableError: (error) => {
      console.log(
        "[SmartDocLoader] React 19 recoverable rendering error:",
        error
      );
    }
  }
});

export { provider as default, provider };
