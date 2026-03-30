import { U as useTranslation, j as jsxRuntimeExports } from './tooltip-DiuRGInA.js';
import { e as __mf_24 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { u as useNavigate, $ as $api } from './styles-CbihN3w-.js';
import { A as ActivityContent } from './activity-content-LvLNslkQ.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-CZIVuyAP.js';
import { P as PersonForm } from './person-form-DKrKoCth.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-CRPQeN8l.js';
import './preload-helper-BgB2ycR-.js';
import './standard-schema-JvgJ9ABX.js';
import './index.esm-DS5i1cns.js';
import './select-form-field-B5SwXID4.js';
import './select-field-DdxeC6qU.js';
import './label-C3rC9dS4.js';
import './index-Dw4N6xbJ.js';
import './text-form-field-ZlRcN4zu.js';

function CreatePerson() {
  const { t } = useTranslation();
  const navigate = useNavigate();
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
