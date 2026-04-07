import { U as useTranslation, j as jsxRuntimeExports, F as Button, E as cn } from './tooltip-Cuc2eMnW.js';
import { n as __mf_28 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { u as useForm, S as Separator } from './index.esm-CTXdo5zr.js';
import { a as TextFormField } from './text-form-field-US_3vH3R.js';
import { d as __mf_115 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { s } from './standard-schema-DUkEvJFp.js';
import { g as getInitialValues, f as fields, s as schema } from './styles-hjhuduQt.js';

function DocumentTypeForm({
  documentType,
  onSubmit,
  loading,
  className,
  ...props
}) {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const { control, handleSubmit, reset } = useForm({
    resolver: s(
      documentType ? schema.update : schema.update
    ),
    defaultValues: getInitialValues(documentType)
  });
  __mf_28(() => {
    if (documentType) {
      reset(getInitialValues(documentType));
    }
  }, [documentType, reset]);
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "form",
    {
      className: cn("form-grid-layout w-full", className),
      "data-cy": "upload-document-form",
      onSubmit: handleSubmit(onSubmit),
      ...props,
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { "data-slot": "form-fields", children: [
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            TextFormField,
            {
              label: t(fields.code.label),
              placeholder: t(fields.code.placeholder),
              control,
              name: "code"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            TextFormField,
            {
              label: t(fields.name.label),
              placeholder: t(fields.name.placeholder),
              control,
              name: "name"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            TextFormField,
            {
              label: t(fields.description.label),
              placeholder: t(fields.description.placeholder),
              control,
              name: "description",
              className: "col-start-full"
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
              onClick: () => navigate("/document-types", { replace: true }),
              disabled: loading,
              children: t("common.buttons.cancel")
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(Button, { type: "submit", className: "px-8", loading, children: t("common.buttons.save") })
        ] })
      ]
    }
  );
}

export { DocumentTypeForm as D };
