import { j as jsxRuntimeExports, U as useTranslation, F as Button, E as cn, V as lodashExports } from './tooltip-DiuRGInA.js';
import { e as __mf_24, d as __mf_38 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { b as useQuery, $ as $api, u as useNavigate, e as fields, l as schema, m as useLocation } from './styles-CbihN3w-.js';
import { A as ActivityContent } from './activity-content-LvLNslkQ.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-CZIVuyAP.js';
import { D as DocumentTypeFormField, b as base64ToFile, f as formatToISODate } from './files.utils-q01t6s1W.js';
import { s } from './standard-schema-JvgJ9ABX.js';
import { u as useForm, S as Separator } from './index.esm-DS5i1cns.js';
import { A as AutocompleteFormField, D as DateFormField } from './popover-BcC5aoGW.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-CEJIZLYi.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-CRPQeN8l.js';
import './preload-helper-BgB2ycR-.js';
import './label-C3rC9dS4.js';
import './dialog-CB2PHVfj.js';
import './index-Dw4N6xbJ.js';
import './useMedia--W0lxxpj.js';

const EMPTY_OPPTIONS = [];
function PersonFormField({
  onOptionChange,
  ...props
}) {
  const { data: options } = useQuery({
    ...$api.queryOptions("get", "/persons", {
      params: {
        query: { size: 1e3 }
      }
    }),
    select: (result) => (result.data ?? []).map((person) => ({
      label: person.name,
      value: person.pid,
      id: person.id
    }))
  });
  const handleOptionChange = __mf_24(
    (value) => {
      if (onOptionChange) {
        onOptionChange(options?.find((option) => option.value === value));
      }
    },
    [options, onOptionChange]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    AutocompleteFormField,
    {
      onValueChange: handleOptionChange,
      ...props,
      options: options ?? EMPTY_OPPTIONS
    }
  );
}

function UploadDocumentForm({
  onSubmit,
  loading,
  className,
  ...props
}) {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [client, setClient] = __mf_38("");
  const { control, handleSubmit } = useForm({
    resolver: s(schema)
  });
  const handleFormSubmit = __mf_24(
    (values) => {
      onSubmit({ ...values, id: client });
    },
    [onSubmit, client]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "form",
    {
      className: cn("form-grid-layout w-full", className),
      "data-cy": "upload-document-form",
      onSubmit: handleSubmit(handleFormSubmit),
      ...props,
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { "data-slot": "form-fields", children: [
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            DocumentTypeFormField,
            {
              mode: "single",
              label: t(fields.type.label),
              placeholder: t(fields.type.placeholder),
              control,
              name: "type"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            PersonFormField,
            {
              mode: "single",
              label: t(fields.personId.label),
              placeholder: t(fields.personId.placeholder),
              control,
              name: "personId",
              onOptionChange: (option) => {
                setClient(option?.id ?? "");
              }
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            DateFormField,
            {
              label: t(fields.date.label),
              placeholder: t(fields.date.placeholder),
              control,
              name: "date"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(Separator, { className: "mt-4" })
        ] }),
        /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { "data-slot": "form-actions", children: [
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            Button,
            {
              className: "px-8",
              variant: "tonal",
              onClick: () => navigate("/"),
              children: t("common.buttons.cancel")
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(Button, { type: "submit", className: "px-8", loading, children: t("common.buttons.save") })
        ] })
      ]
    }
  );
}

function UploadDocument() {
  const { t } = useTranslation();
  const { state } = useLocation();
  const [client, setClient] = __mf_38("");
  const navigate = useNavigate();
  const createCallbacks = useMutationCallbacks("/documents", {
    successMessage: t("documents.messages.upload.success"),
    errorMessage: t("documents.messages.upload.error"),
    onSuccess: () => navigate(`/documents?client=${client}`, { replace: true })
  });
  const { mutate: uploadDocument, isPending } = $api.useMutation(
    "post",
    "/documents",
    {
      ...createCallbacks
    }
  );
  const handleSubmit = __mf_24(
    (body) => {
      const formData = new FormData();
      if (state?.image) {
        formData.append("document", base64ToFile(state.image, state.fileName));
      }
      setClient(body.id);
      uploadDocument({
        body: formData,
        params: {
          query: {
            ...lodashExports.pick(body, ["type", "personId"]),
            date: formatToISODate(body.date) || void 0
          }
        }
      });
    },
    [uploadDocument, state]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    ActivityContent,
    {
      title: t("routes.upload-document.title"),
      subtitle: t("routes.upload-document.description"),
      children: /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "flex flex-col gap-2 w-xs md:w-sm max-w-full w-full bg-background-light", children: [
        state?.image && /* @__PURE__ */ jsxRuntimeExports.jsx(
          "img",
          {
            src: state.image,
            alt: "Preview",
            className: "max-h-64 maxw-64 object-contain"
          }
        ),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          UploadDocumentForm,
          {
            onSubmit: handleSubmit,
            loading: isPending,
            className: "w-full"
          }
        )
      ] })
    }
  );
}

export { UploadDocument };
