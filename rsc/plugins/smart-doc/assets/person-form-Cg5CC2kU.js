import { U as useTranslation, j as jsxRuntimeExports, F as Button, E as cn } from './tooltip-Cuc2eMnW.js';
import { s } from './standard-schema-DUkEvJFp.js';
import { i as __mf_34, n as __mf_28 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { u as useForm, S as Separator } from './index.esm-CTXdo5zr.js';
import { d as __mf_115 } from './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { S as SelectFormField } from './select-form-field-yYq_MzGz.js';
import { a as TextFormField } from './text-form-field-US_3vH3R.js';
import { l as getInitialValues, z as zGender, j as fields, m as schema } from './styles-hjhuduQt.js';

function PersonForm({
  person,
  onSubmit,
  loading,
  className,
  ...props
}) {
  const { t } = useTranslation();
  const navigate = __mf_115();
  const { control, handleSubmit, reset } = useForm({
    resolver: s(
      person ? schema.update : schema.update
    ),
    defaultValues: getInitialValues(person)
  });
  const genderOptions = __mf_34(
    () => zGender.options.map((value) => ({
      value,
      label: t(`persons.genders.${value}`)
    })),
    [t]
  );
  __mf_28(() => {
    if (person) {
      reset(getInitialValues(person));
    }
  }, [person, reset]);
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
              label: t(fields.pid.label),
              placeholder: t(fields.pid.placeholder),
              control,
              name: "pid",
              type: "number"
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
              label: t(fields.email.label),
              placeholder: t(fields.email.placeholder),
              control,
              name: "email"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            TextFormField,
            {
              label: t(fields.phoneNumber.label),
              placeholder: t(fields.phoneNumber.placeholder),
              control,
              name: "phoneNumber"
            }
          ),
          /* @__PURE__ */ jsxRuntimeExports.jsx(
            SelectFormField,
            {
              label: t(fields.gender.label),
              placeholder: t(fields.gender.placeholder),
              control,
              name: "gender",
              options: genderOptions
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

export { PersonForm as P };
