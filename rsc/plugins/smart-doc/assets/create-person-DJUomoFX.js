import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-Cuc2eMnW.js';
import { f as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { d as __mf_115 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { $ as $api } from './styles-hjhuduQt.js';
import { A as ActivityContent } from './activity-content-C-8DsC1V.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-BaJ_WT_z.js';
import { P as PersonForm } from './person-form-Cg5CC2kU.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js';
import './preload-helper-BgB2ycR-.js';
import './standard-schema-DUkEvJFp.js';
import './index.esm-CTXdo5zr.js';
import './select-form-field-yYq_MzGz.js';
import './select-field-CSwbPc9r.js';
import './label-WCiYXR7h.js';
import './index-z1Zuky25.js';
import './text-form-field-US_3vH3R.js';

function CreatePerson() {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const createCallbacks = useMutationCallbacks("/persons", {
    successMessage: t("persons.messages.create.success"),
    errorMessage: t("persons.messages.create.error"),
    onSuccess: () => navigate("..", { replace: true })
  });
  const { mutate: createPerson, isPending } = $api.useMutation(
    "post",
    "/persons",
    {
      ...createCallbacks
    }
  );
  const handleSubmit = __mf_24(
    (body) => {
      createPerson({ body });
    },
    [createPerson]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.upload-document.title"),
      subtitle: t("routes.upload-document.description"),
      children: /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex flex-col gap-2 w-xs md:w-sm max-w-full w-full bg-background-light", children: /* @__PURE__ */ jsxRuntimeExports.jsx(PersonForm, { onSubmit: handleSubmit, loading: isPending }) })
    }
  );
}

export { CreatePerson };
