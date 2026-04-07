import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-Cuc2eMnW.js';
import { u as useQuery, $ as $api, Q as QueryClient } from './styles-hjhuduQt.js';
import { B as __mf_29, n as __mf_28, f as __mf_24, C as __mf_0 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { d as __mf_115, e as __mf_120 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
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

const loader = async ({ params }) => {
  const queryClient = new QueryClient();
  await queryClient.prefetchQuery(
    $api.queryOptions("get", "/persons/{id}", {
      params: { path: { id: params.id ?? "" } }
    })
  );
};
function UpdatePerson() {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const params = __mf_120();
  const { data: person, error } = useQuery({
    ...$api.queryOptions("get", "/persons/{id}", {
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
  const updateCallbacks = useMutationCallbacks("/persons", {
    successMessage: t("persons.messages.update.success"),
    errorMessage: t("persons.messages.update.error"),
    onSuccess: () => navigate("..", { replace: true })
  });
  const { mutate: updatePerson, isPending } = $api.useMutation(
    "patch",
    "/persons/{id}",
    {
      ...updateCallbacks
    }
  );
  const handleSubmit = __mf_24(
    (values) => {
      updatePerson({
        body: values,
        params: {
          path: { id: params.id ?? "" }
        }
      });
    },
    [updatePerson, params.id]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.upload-document.title"),
      subtitle: t("routes.upload-document.description"),
      children: /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex flex-col gap-2 w-xs md:w-sm max-w-full w-full bg-background-light", children: /* @__PURE__ */ jsxRuntimeExports.jsx(__mf_0, { mode: person ? "visible" : "hidden", children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        PersonForm,
        {
          person,
          onSubmit: handleSubmit,
          loading: isPending
        }
      ) }) })
    }
  );
}

export { UpdatePerson, loader };
