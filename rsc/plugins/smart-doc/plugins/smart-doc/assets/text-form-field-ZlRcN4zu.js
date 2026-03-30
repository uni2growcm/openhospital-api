import { j as jsxRuntimeExports, E as cn } from './tooltip-DiuRGInA.js';
import { C as Controller } from './index.esm-DS5i1cns.js';
import { B as __mf_30 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { L as Label, H as HelperText } from './label-C3rC9dS4.js';

const inputDefaultClassNames = [
  "inline-flex gap-2 items-center h-11 w-full bg-muted/5 not-placeholder-shown:bg-primary/10 disabled:bg-transparent",
  "file:text-muted-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground border-input",
  "rounded border px-2 py-1 text-base shadow-xs transition-all outline-none file:inline-flex",
  "file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-medium",
  "disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
  "focus-visible:border-primary focus-visible:ring-primary/30 focus-visible:ring-[2px]",
  "aria-invalid:ring-error/20 dark:aria-invalid:ring-error/40 aria-invalid:border-error"
];
function Input({ className, type = "text", ...props }) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    "input",
    {
      type,
      "data-slot": "input",
      className: cn(inputDefaultClassNames, className),
      "data-cy": props.name ?? "input",
      ...props
    }
  );
}

function TextField({
  error,
  helperText,
  label,
  className,
  leading,
  trailing,
  ...props
}) {
  const id = __mf_30();
  const component = /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      className: cn("relative", {
        "*:first:size-8 *:first:absolute *:first:top-[calc(50%-16px)] *:first:left-1.5 [&_input]:pl-10 *:first:bg-muted/80 *:first:text-muted-foreground": !!leading,
        "*:last:size-8 *:last:absolute *:last:top-[calc(50%-16px)] *:last:right-1.5 [&_input]:pr-.5 *:last:bg-muted/80  *:last:text-muted-foreground": !!trailing
      }),
      children: [
        leading && leading,
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          Input,
          {
            "aria-invalid": error,
            ...props,
            id: props.id ?? id,
            className: cn(
              "transition-all hover:border-primary hover:ring-[2px] hover:ring-primary/25"
            )
          }
        ),
        trailing && trailing
      ]
    }
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      "data-slot": "text-field",
      className: cn("grid w-full gap-1.5", className),
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Label, { withAsterisk: props.required, htmlFor: props.id ?? id, children: label }),
        helperText ? /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "flex flex-col gap-0.5", children: [
          component,
          /* @__PURE__ */ jsxRuntimeExports.jsx(HelperText, { error, children: helperText })
        ] }) : component
      ]
    }
  );
}

function TextFormField({
  control,
  name,
  ...props
}) {
  const handleChange = (field) => (event) => {
    if (props.type === "number") {
      const value = event.target.valueAsNumber;
      field.onChange(Number.isNaN(value) ? 0 : value);
    } else field.onChange(event);
  };
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Controller,
    {
      control,
      name,
      render: ({ field, fieldState }) => /* @__PURE__ */ jsxRuntimeExports.jsx(
        TextField,
        {
          "data-cy": field.name,
          ...props,
          id: props.id ?? field.name,
          "aria-invalid": fieldState.invalid,
          name: field.name,
          value: field.value ?? "",
          disabled: field.disabled,
          onBlur: field.onBlur,
          onChange: handleChange(field),
          error: fieldState.invalid,
          helperText: fieldState.error?.message
        }
      )
    }
  );
}

export { Input as I, TextField as T, TextFormField as a, inputDefaultClassNames as i };
