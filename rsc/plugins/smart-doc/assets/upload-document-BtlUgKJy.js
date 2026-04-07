import { j as jsxRuntimeExports, U as useTranslation, F as Button, E as cn, V as lodashExports } from './tooltip-Cuc2eMnW.js';
import { f as __mf_24, e as __mf_38 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { d as __mf_115, g as __mf_112 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { u as useQuery, $ as $api, a as fields, i as schema } from './styles-hjhuduQt.js';
import { A as ActivityContent } from './activity-content-C-8DsC1V.js';
import { u as useMutationCallbacks } from './use-mutation-callbacks-BaJ_WT_z.js';
import { D as DocumentTypeFormField, b as base64ToFile, f as formatToISODate } from './files.utils-DGNQ2W30.js';
import { s } from './standard-schema-DUkEvJFp.js';
import { u as useForm, S as Separator } from './index.esm-CTXdo5zr.js';
import { A as AutocompleteFormField, D as DateFormField } from './popover-BPSs3yej.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs_commonjs-proxy-COy7YPAX.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './runtimeInit-CEsLEwNP.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs_commonjs-proxy-ByWZy8-6.js';
import './preload-helper-BgB2ycR-.js';
import './label-WCiYXR7h.js';
import './dialog-DURgvrE0.js';
import './index-z1Zuky25.js';
import './useMedia-ewnkBszT.js';

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
  const navigate = __mf_115();
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
  const { state } = __mf_112();
  const [client, setClient] = __mf_38("");
  const navigate = __mf_115();
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
