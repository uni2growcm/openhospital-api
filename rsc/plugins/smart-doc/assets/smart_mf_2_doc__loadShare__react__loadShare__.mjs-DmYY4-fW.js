import { i as initPromise } from './runtimeInit-CEsLEwNP.js';

function _mergeNamespaces(n, m) {
  for (var i = 0; i < m.length; i++) {
    const e = m[i];
    if (typeof e !== 'string' && !Array.isArray(e)) { for (const k in e) {
      if (k !== 'default' && !(k in n)) {
        const d = Object.getOwnPropertyDescriptor(e, k);
        if (d) {
          Object.defineProperty(n, k, d.get ? d : {
            enumerable: true,
            get: () => e[k]
          });
        }
      }
    } }
  }
  return Object.freeze(Object.defineProperty(n, Symbol.toStringTag, { value: 'Module' }));
}

const res = initPromise.then(runtime => runtime.loadShare("react", {
      customShareInfo: {shareConfig:{
        singleton: true,
        strictVersion: false,
        requiredVersion: "^19.2.4"
      }}
    }));
    const exportModule = await res.then((factory) => (typeof factory === "function" ? factory() : factory));
    const __moduleExports = exportModule;
const React3 = exportModule.__esModule ? exportModule.default : exportModule;
    const { Activity: __mf_0, Children: __mf_1, Component: __mf_2, Fragment: __mf_3, Profiler: __mf_4, PureComponent: __mf_5, StrictMode: __mf_6, Suspense: __mf_7, __CLIENT_INTERNALS_DO_NOT_USE_OR_WARN_USERS_THEY_CANNOT_UPGRADE: __mf_8, __COMPILER_RUNTIME: __mf_9, cache: __mf_10, cacheSignal: __mf_11, cloneElement: __mf_12, createContext: __mf_13, createElement: __mf_14, createRef: __mf_15, forwardRef: __mf_16, isValidElement: __mf_17, lazy: __mf_18, memo: __mf_19, startTransition: __mf_20, unstable_useCacheRefresh: __mf_21, use: __mf_22, useActionState: __mf_23, useCallback: __mf_24, useContext: __mf_25, useDebugValue: __mf_26, useDeferredValue: __mf_27, useEffect: __mf_28, useEffectEvent: __mf_29, useId: __mf_30, useImperativeHandle: __mf_31, useInsertionEffect: __mf_32, useLayoutEffect: __mf_33, useMemo: __mf_34, useOptimistic: __mf_35, useReducer: __mf_36, useRef: __mf_37, useState: __mf_38, useSyncExternalStore: __mf_39, useTransition: __mf_40, version: __mf_41 } = exportModule;

const React4 = /*#__PURE__*/_mergeNamespaces({
  __proto__: null,
  Activity: __mf_0,
  Children: __mf_1,
  Component: __mf_2,
  Fragment: __mf_3,
  Profiler: __mf_4,
  PureComponent: __mf_5,
  StrictMode: __mf_6,
  Suspense: __mf_7,
  __CLIENT_INTERNALS_DO_NOT_USE_OR_WARN_USERS_THEY_CANNOT_UPGRADE: __mf_8,
  __COMPILER_RUNTIME: __mf_9,
  cache: __mf_10,
  cacheSignal: __mf_11,
  cloneElement: __mf_12,
  createContext: __mf_13,
  createElement: __mf_14,
  createRef: __mf_15,
  default: React3,
  forwardRef: __mf_16,
  isValidElement: __mf_17,
  lazy: __mf_18,
  memo: __mf_19,
  startTransition: __mf_20,
  unstable_useCacheRefresh: __mf_21,
  use: __mf_22,
  useActionState: __mf_23,
  useCallback: __mf_24,
  useContext: __mf_25,
  useDebugValue: __mf_26,
  useDeferredValue: __mf_27,
  useEffect: __mf_28,
  useEffectEvent: __mf_29,
  useId: __mf_30,
  useImperativeHandle: __mf_31,
  useInsertionEffect: __mf_32,
  useLayoutEffect: __mf_33,
  useMemo: __mf_34,
  useOptimistic: __mf_35,
  useReducer: __mf_36,
  useRef: __mf_37,
  useState: __mf_38,
  useSyncExternalStore: __mf_39,
  useTransition: __mf_40,
  version: __mf_41
}, [__moduleExports]);

export { __mf_7 as A, __mf_29 as B, __mf_0 as C, React4 as R, __mf_14 as _, React3 as a, __mf_2 as b, __mf_13 as c, __mf_37 as d, __mf_38 as e, __mf_24 as f, __mf_33 as g, __mf_25 as h, __mf_34 as i, __mf_41 as j, __mf_17 as k, __mf_1 as l, __mf_12 as m, __mf_28 as n, __mf_16 as o, __mf_3 as p, __mf_26 as q, __mf_31 as r, __mf_19 as s, __mf_6 as t, __mf_39 as u, __mf_5 as v, __mf_15 as w, __mf_36 as x, __mf_20 as y, __mf_30 as z };
