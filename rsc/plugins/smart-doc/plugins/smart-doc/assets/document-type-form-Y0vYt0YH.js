import { U as useTranslation, j as jsxRuntimeExports, F as Button, E as cn } from './tooltip-DiuRGInA.js';
import { s } from './standard-schema-JvgJ9ABX.js';
import { m as __mf_28 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { u as useForm, S as Separator } from './index.esm-DS5i1cns.js';
import { u as useNavigate, g as getInitialValues, f as fields, s as schema } from './styles-B1l4Bvay.js';
import { a as TextFormField } from './text-form-field-ZlRcN4zu.js';

function DocumentTypeForm({
  documentType,
  onSubmit,
  loading,
  className,
  ...props
}) {
  const { t } = useTranslation();
  const navigate = useNavigate();
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
          /* @__PURE__ */ jsxRuntimeExports.jsx(Button, { className: "px-8", variant: "tonal", onClick: () => navigate(-1), children: t("common.buttons.cancel") }),
          /* @__PURE__ */ jsxRuntimeExports.jsx(Button, { type: "submit", className: "px-8", loading, children: t("common.buttons.save") })
        ] })
      ]
    }
  );
}

export { DocumentTypeForm as D };
