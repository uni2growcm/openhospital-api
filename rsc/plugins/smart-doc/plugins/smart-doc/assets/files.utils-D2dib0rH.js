import { j as jsxRuntimeExports } from './tooltip-DiuRGInA.js';
import { b as useQuery, $ as $api } from './styles-B1l4Bvay.js';
import './smart_mf_2_doc__loadShare__react__loadShare__.mjs-BkoayUfL.js';
import { A as AutocompleteFormField, p as isValid, q as format } from './popover-BcC5aoGW.js';

const EMPTY_OPPTIONS = [];
function DocumentTypeFormField({
  ...props
}) {
  const { data: options } = useQuery({
    ...$api.queryOptions("get", "/document-types", {
      params: {
        query: {}
      }
    }),
    select: (result) => result.map((type) => ({
      label: type.name,
      value: type.code
    }))
  });
  return /* @__PURE__ */ jsxRuntimeExports.jsx(AutocompleteFormField, { ...props, options: options ?? EMPTY_OPPTIONS });
}

function formatToISODate(date) {
  if (!date) return null;
  return isValid(date) ? format(date, "yyyy-MM-dd") : "";
}

const IMAGE_EXTENSIONS = ["jpg", "jpeg", "png", "bmp", "webp", "svg"];
function base64ToFile(dataUrl, fileName) {
  const [meta, base64Data] = dataUrl.split(",");
  if (!meta || !base64Data) {
    throw new Error("Invalid Base64 data URL");
  }
  const mimeType = meta.match(/data:(.*);base64/)?.[1];
  if (!mimeType) {
    throw new Error("Could not determine MIME type");
  }
  const byteString = atob(base64Data);
  const bytes = new Uint8Array(byteString.length);
  for (let i = 0; i < byteString.length; i++) {
    bytes[i] = byteString.charCodeAt(i);
  }
  return new File(
    [bytes],
    `${fileName?.split(".")?.[0] || crypto.randomUUID().slice(19)}.${mimeType.split("/").pop()}`,
    { type: mimeType }
  );
}

export { DocumentTypeFormField as D, IMAGE_EXTENSIONS as I, base64ToFile as b, formatToISODate as f };
