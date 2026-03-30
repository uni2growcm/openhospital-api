const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["assets/export-app-CBQbd0vn.js","assets/styles-CbihN3w-.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js","assets/runtimeInit-CEsLEwNP.js","assets/smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-CRPQeN8l.js","assets/smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js","assets/tooltip-DiuRGInA.js","assets/preload-helper-BgB2ycR-.js","assets/index-B2n17hA6.js","assets/dialog-custom-CB9UiEiC.js","assets/dialog-CB2PHVfj.js","assets/index-Dw4N6xbJ.js","assets/useMedia--W0lxxpj.js","assets/index.esm-DS5i1cns.js","assets/popover-BcC5aoGW.js","assets/label-C3rC9dS4.js","assets/table-XMtuV7nB.js","assets/select-field-DdxeC6qU.js","assets/text-form-field-ZlRcN4zu.js","assets/select-form-field-B5SwXID4.js"])))=>i.map(i=>d[i]);
import { _ as __vitePreload } from './preload-helper-BgB2ycR-.js';

const exposesMap = {
    
        "./app": async () => {
          const importModule = await __vitePreload(() => import('./export-app-CBQbd0vn.js'),true              ?__vite__mapDeps([0,1,2,3,4,5,6,7,8]):void 0);
          const exportModule = {};
          Object.assign(exportModule, importModule);
          Object.defineProperty(exportModule, "__esModule", {
            value: true,
            enumerable: false
          });
          return exportModule
        }
      ,
        "./widgets": async () => {
          const importModule = await __vitePreload(() => import('./index-B2n17hA6.js'),true              ?__vite__mapDeps([9,10,7,2,3,4,6,11,12,13,14,15,16,17,18,19,20]):void 0);
          const exportModule = {};
          Object.assign(exportModule, importModule);
          Object.defineProperty(exportModule, "__esModule", {
            value: true,
            enumerable: false
          });
          return exportModule
        }
      
  };

export { exposesMap as default };
