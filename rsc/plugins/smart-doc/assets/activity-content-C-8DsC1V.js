import { j as jsxRuntimeExports, E as cn, O as Typography } from './tooltip-Cuc2eMnW.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';

function getNextPageParam(lastPage) {
  const page = lastPage?.metadata?.page ?? 0;
  return page + 1 < (lastPage?.metadata?.totalPages ?? 0) ? page + 1 : void 0;
}
const INFINITE_QUERY_DEFAULT_OPTIONS = {
  initialPageParam: null,
  pageParamName: "page",
  getNextPageParam
};
const queryMatchPredicate = (...matches) => (query) => {
  const queryString = JSON.stringify(query.queryKey);
  return matches.some((match) => queryString.includes(match));
};

function Background({
  children,
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      "data-slot": "background",
      className: cn(
        "flex flex-col size-lv relative bg-gradient-to-r from-primary/5 to-primary/20",
        className
      ),
      ...props,
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          "div",
          {
            className: "absolute w-32 bottom-1/3 aspect-square bg-gradient-to-r from-primary to-secondary rounded-full blur-3xl"
          }
        ),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          "div",
          {
            className: "absolute bottom-0 right-0 w-32 max-w-md aspect-square bg-gradient-to-r from-primary to-secondary rounded-full blur-3xl "
          }
        ),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          "div",
          {
            className: "absolute w-40 right-1/2 aspect-square bg-gradient-to-r from-primary/60 to-secondary/60 rounded-full blur-3xl"
          }
        ),
        /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex size-full flex-1 z-5 center max-sm:items-start", children })
      ]
    }
  );
}

function ActivityContent({
  title,
  subtitle,
  children,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Background, { "data-cy": "activity-content", ...props, children: [
    title && /* @__PURE__ */ jsxRuntimeExports.jsx("title", { "data-cy": "title", children: title }),
    /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "grid m-4 p-4 bg-background/60 gap-4 h-min w-max", children: [
      /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex gap-2 w-full grid **:text-center", children: (title || subtitle) && /* @__PURE__ */ jsxRuntimeExports.jsxs(jsxRuntimeExports.Fragment, { children: [
        title && /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { variant: "title-medium", children: title }),
        subtitle && /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { children: subtitle })
      ] }) }),
      children
    ] })
  ] });
}

export { ActivityContent as A, INFINITE_QUERY_DEFAULT_OPTIONS as I, queryMatchPredicate as q };
