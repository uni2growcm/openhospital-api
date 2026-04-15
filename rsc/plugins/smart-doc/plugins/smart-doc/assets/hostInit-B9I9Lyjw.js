const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["smart-doc.js","assets/index.cjs-C_kOjFta.js","assets/virtualExposes-BoR9opGo.js","assets/preload-helper-Dtwkx9f4.js","assets/runtimeInit-CEsLEwNP.js"])))=>i.map(i=>d[i]);
import { _ as __vitePreload } from './preload-helper-Dtwkx9f4.js';

const remoteEntryPromise = __vitePreload(() => import('../smart-doc.js'),true              ?__vite__mapDeps([0,1,2,3,4]):void 0);
    Promise.resolve(remoteEntryPromise)
      .then(remoteEntry => {
        return Promise.resolve(remoteEntry.__tla)
          .then(remoteEntry.init)
          .catch(remoteEntry.init)
      });
