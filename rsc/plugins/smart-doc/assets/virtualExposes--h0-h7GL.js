const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["assets/export-app-Dr5svuLf.js","assets/styles-hjhuduQt.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js","assets/smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js","assets/runtimeInit-CEsLEwNP.js","assets/smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js","assets/smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js","assets/tooltip-Cuc2eMnW.js","assets/smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js","assets/preload-helper-BgB2ycR-.js","assets/index-CDIYsoST.js","assets/dialog-custom-D5WSdJMu.js","assets/dialog-DURgvrE0.js","assets/index-z1Zuky25.js","assets/useMedia-ewnkBszT.js","assets/index.esm-CTXdo5zr.js","assets/popover-BPSs3yej.js","assets/label-WCiYXR7h.js","assets/table-CmSAdCry.js","assets/select-field-CSwbPc9r.js","assets/text-form-field-US_3vH3R.js","assets/select-form-field-yYq_MzGz.js"])))=>i.map(i=>d[i]);
import { _ as __vitePreload } from './preload-helper-BgB2ycR-.js';

const exposesMap = {
    
        "./app": async () => {
          const importModule = await __vitePreload(() => import('./export-app-Dr5svuLf.js'),true              ?__vite__mapDeps([0,1,2,3,4,5,6,7,8,9]):void 0);
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
          const importModule = await __vitePreload(() => import('./index-CDIYsoST.js'),true              ?__vite__mapDeps([10,11,7,2,3,4,6,12,13,14,15,16,17,8,18,19,20,21]):void 0);
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
