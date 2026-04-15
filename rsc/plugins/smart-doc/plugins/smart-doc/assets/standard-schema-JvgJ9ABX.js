import { g as get, s as set } from './index.esm-DS5i1cns.js';

const r=(t,r,o)=>{if(t&&"reportValidity"in t){const s=get(o,r);t.setCustomValidity(s&&s.message||""),t.reportValidity();}},o=(e,t)=>{for(const o in t.fields){const s=t.fields[o];s&&s.ref&&"reportValidity"in s.ref?r(s.ref,o,e):s&&s.refs&&s.refs.forEach(t=>r(t,o,e));}},s$1=(r,s)=>{s.shouldUseNativeValidation&&o(r,s);const n={};for(const o in r){const f=get(s.fields,o),c=Object.assign(r[o]||{},{ref:f&&f.ref});if(i(s.names||Object.keys(r),o)){const r=Object.assign({},get(n,o));set(r,"root",c),set(n,o,r);}else set(n,o,c);}return n},i=(e,t)=>{const r=n$1(t);return e.some(e=>n$1(e).match(`^${r}\\.\\d+`))};function n$1(e){return e.replace(/\]|\[/g,"")}

// src/getDotPath/getDotPath.ts
function getDotPath(issue) {
  if (issue.path?.length) {
    let dotPath = "";
    for (const item of issue.path) {
      const key = typeof item === "object" ? item.key : item;
      if (typeof key === "string" || typeof key === "number") {
        if (dotPath) {
          dotPath += `.${key}`;
        } else {
          dotPath += key;
        }
      } else {
        return null;
      }
    }
    return dotPath;
  }
  return null;
}

function n(){return n=Object.assign?Object.assign.bind():function(e){for(var r=1;r<arguments.length;r++){var t=arguments[r];for(var n in t)({}).hasOwnProperty.call(t,n)&&(e[n]=t[n]);}return e},n.apply(null,arguments)}function s(s,a,i){return void 0===i&&(i={}),function(a,o$1,u){try{var l=function(){if(c.issues){var s=function(e,r){for(var s={},a=0;a<e.length;a++){var i=e[a],o=getDotPath(i);if(o&&(s[o]||(s[o]={message:i.message,type:""}),r)){var u,l=s[o].types||{};s[o].types=n({},l,((u={})[Object.keys(l).length]=i.message,u));}}return s}(c.issues,!u.shouldUseNativeValidation&&"all"===u.criteriaMode);return {values:{},errors:s$1(s,u)}}return u.shouldUseNativeValidation&&o({},u),{values:i.raw?Object.assign({},a):c.value,errors:{}}},c=s["~standard"].validate(a),f=function(){if(c instanceof Promise)return Promise.resolve(c).then(function(e){c=e;})}();return Promise.resolve(f&&f.then?f.then(l):l())}catch(e){return Promise.reject(e)}}}

export { s };
