import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-Cuc2eMnW.js';
import { f as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { d as __mf_115 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { $ as $api } from './styles-hjhuduQt.js';
import { A as ActivityContent } from './activity-content-C-8DsC1V.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-BaJ_WT_z.js';
import { D as DocumentTypeForm } from './document-type-form-DUgDK5AN.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js';
import './preload-helper-BgB2ycR-.js';
import './index.esm-CTXdo5zr.js';
import './text-form-field-US_3vH3R.js';
import './label-WCiYXR7h.js';
import './standard-schema-DUkEvJFp.js';

function CreateDocumentType() {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const createCallbacks = useMutationCallbacks("/document-types", {
    successMessage: t("document-types.messages.create.success"),
    errorMessage: t("document-types.messages.create.error"),
    onSuccess: () => navigate("..", { replace: true })
  });
  const { mutate: createDocumentType, isPending } = $api.useMutation(
    "post",
    "/document-types",
    {
      ...createCallbacks
    }
  );
  const handleSubmit = __mf_24(
    (body) => {
      createDocumentType({ body });
    },
    [createDocumentType]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.upload-document.title"),
      subtitle: t("routes.upload-document.description"),
      children: /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex flex-col gap-2 w-xs md:w-sm max-w-full w-full bg-background-light", children: /* @__PURE__ */ jsxRuntimeExports.jsx(DocumentTypeForm, { onSubmit: handleSubmit, loading: isPending }) })
    }
  );
}

export { CreateDocumentType };
