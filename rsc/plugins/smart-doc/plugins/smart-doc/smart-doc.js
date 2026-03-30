const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["assets/index-JdM9gL9A.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js","assets/runtimeInit-CEsLEwNP.js","assets/index-BxNQRtV4.js"])))=>i.map(i=>d[i]);
import { i as init_1 } from './assets/index.cjs-C_kOjFta.js';
import exposesMap from './assets/virtualExposes-BoR9opGo.js';
import { _ as __vitePreload } from './assets/preload-helper-Dtwkx9f4.js';
import { a as initResolve } from './assets/runtimeInit-CEsLEwNP.js';

const importMap = {
      
        "react": async () => {
          let pkg = await __vitePreload(() => import('./assets/index-JdM9gL9A.js').then(n => n.i),true              ?__vite__mapDeps([0,1,2,3]):void 0);
            return pkg;
        }
      ,
        "react-dom": async () => {
          let pkg = await __vitePreload(() => import('./assets/index-BxNQRtV4.js').then(n => n.i),true              ?__vite__mapDeps([4,1,2,3]):void 0);
            return pkg;
        }
      
    };
      const usedShared = {
      
          "react": {
            name: "react",
            version: "19.2.4",
            scope: ["default"],
            loaded: false,
            from: "smart-doc",
            async get () {
              usedShared["react"].loaded = true;
              const {"react": pkgDynamicImport} = importMap;
              const res = await pkgDynamicImport();
              const exportModule = {...res};
              // All npm packages pre-built by vite will be converted to esm
              Object.defineProperty(exportModule, "__esModule", {
                value: true,
                enumerable: false
              });
              return function () {
                return exportModule
              }
            },
            shareConfig: {
              singleton: true,
              requiredVersion: "^19.2.4",
              
            }
          }
        ,
          "react-dom": {
            name: "react-dom",
            version: "19.2.4",
            scope: ["default"],
            loaded: false,
            from: "smart-doc",
            async get () {
              usedShared["react-dom"].loaded = true;
              const {"react-dom": pkgDynamicImport} = importMap;
              const res = await pkgDynamicImport();
              const exportModule = {...res};
              // All npm packages pre-built by vite will be converted to esm
              Object.defineProperty(exportModule, "__esModule", {
                value: true,
                enumerable: false
              });
              return function () {
                return exportModule
              }
            },
            shareConfig: {
              singleton: true,
              requiredVersion: "^19.2.4",
              
            }
          }
        
    };
      const usedRemotes = [
      ];

const initTokens = {};
  const shareScopeName = "default";
  const mfName = "smart-doc";
  async function init(shared = {}, initScope = []) {
    const initRes = init_1({
      name: mfName,
      remotes: usedRemotes,
      shared: usedShared,
      plugins: [],
      shareStrategy: 'loaded-first'
    });
    // handling circular init calls
    var initToken = initTokens[shareScopeName];
    if (!initToken)
      initToken = initTokens[shareScopeName] = { from: mfName };
    if (initScope.indexOf(initToken) >= 0) return;
    initScope.push(initToken);
    initRes.initShareScopeMap('default', shared);
    initResolve(initRes);
    try {
      await Promise.all(await initRes.initializeSharing('default', {
        strategy: 'loaded-first',
        from: "build",
        initScope
      }));
    } catch (e) {
      console.error(e);
    }
    return initRes
  }

  function getExposes(moduleName) {
    if (!(moduleName in exposesMap)) throw new Error(`Module ${moduleName} does not exist in container.`)
    return (exposesMap[moduleName])().then(res => () => res)
  }

export { getExposes as get, init };
