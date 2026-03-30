import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-DiuRGInA.js';
import { e as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { u as useNavigate, $ as $api } from './styles-B1l4Bvay.js';
import { A as ActivityContent } from './activity-content-LvLNslkQ.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-RKd7cPj5.js';
import { D as DocumentTypeForm } from './document-type-form-Y0vYt0YH.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-CRPQeN8l.js';
import './preload-helper-Dtwkx9f4.js';
import './standard-schema-JvgJ9ABX.js';
import './index.esm-DS5i1cns.js';
import './text-form-field-ZlRcN4zu.js';
import './label-C3rC9dS4.js';

function CreateDocumentType() {
  const { t } = useTranslation();
  const navigate = useNavigate();
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
