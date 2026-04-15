import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-DiuRGInA.js';
import { u as useNavigate, a as useParams, b as useQuery, $ as $api, Q as QueryClient } from './styles-B1l4Bvay.js';
import { z as __mf_29, m as __mf_28, e as __mf_24, A as __mf_0 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
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

const loader = async ({ params }) => {
  const queryClient = new QueryClient();
  await queryClient.prefetchQuery(
    $api.queryOptions("get", "/document-types/{id}", {
      params: { path: { id: params.id ?? "" } }
    })
  );
};
function UpdateDocumentType() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const params = useParams();
  const { data: documentType, error } = useQuery({
    ...$api.queryOptions("get", "/document-types/{id}", {
      params: { path: { id: params.id ?? "" } }
    })
  });
  const onErrorEvent = __mf_29((_errorMessage) => {
    navigate("..");
  });
  __mf_28(() => {
    if (error) {
      onErrorEvent(error?.title ?? "");
    }
  }, [error]);
  const updateCallbacks = useMutationCallbacks("/document-types", {
    successMessage: t("document-types.messages.update.success"),
    errorMessage: t("document-types.messages.update.error"),
    onSuccess: () => navigate("..", { replace: true })
  });
  const { mutate: updateDocumentType, isPending } = $api.useMutation(
    "patch",
    "/document-types/{id}",
    {
      ...updateCallbacks
    }
  );
  const handleSubmit = __mf_24(
    (values) => {
      updateDocumentType({
        body: values,
        params: {
          path: { id: params.id ?? "" }
        }
      });
    },
    [updateDocumentType, params.id]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.upload-document.title"),
      subtitle: t("routes.upload-document.description"),
      children: /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex flex-col gap-2 w-xs md:w-sm max-w-full w-full bg-background-light", children: /* @__PURE__ */ jsxRuntimeExports.jsx(__mf_0, { mode: documentType ? "visible" : "hidden", children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        DocumentTypeForm,
        {
          documentType,
          onSubmit: handleSubmit,
          loading: isPending
        }
      ) }) })
    }
  );
}

export { UpdateDocumentType, loader };
