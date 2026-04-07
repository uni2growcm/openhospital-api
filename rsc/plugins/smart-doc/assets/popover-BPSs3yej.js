import { c as createLucideIcon, j as jsxRuntimeExports, E as cn, H as variants, F as Button$1, W as createSlot$1, Y as useId, Z as composeRefs, X as X$1, O as Typography, V as lodashExports, _ as useControllableState, $ as Root2$1, a0 as Anchor, a1 as Presence, a2 as Portal$2, a3 as useComposedRefs, a4 as composeEventHandlers, a5 as createPopperScope, a6 as createContextScope, a7 as DismissableLayer, a8 as Content$1, a9 as Arrow } from './tooltip-Cuc2eMnW.js';
import { C as Controller } from './index.esm-CTXdo5zr.js';
import { a as React3, c as __mf_13, h as __mf_25, f as __mf_24, d as __mf_37, g as __mf_33, e as __mf_38, n as __mf_28, i as __mf_34, o as __mf_16, _ as __mf_14, k as __mf_17, m as __mf_12, u as __mf_39, l as __mf_1, p as __mf_3 } from './smart_mf_2_doc__loadShare__react__loadShare__.mjs-DmYY4-fW.js';
import { L as Label, H as HelperText } from './label-WCiYXR7h.js';
import { R as Root$1, P as Portal$1, O as Overlay, C as Content, D as Dialog, e as DialogHeader, h as DialogTitle, c as DialogDescription, b as DialogContent, i as DialogTrigger } from './dialog-DURgvrE0.js';
import './smart_mf_2_doc__loadShare__react_mf_2_dom__loadShare__.mjs-DE-Jm_ka.js';
import './smart_mf_2_doc__loadShare__react_mf_2_router__loadShare__.mjs-DYTOv3NH.js';
import { a as ChevronLeft, C as ChevronRight, u as useMedia, v as variants$1 } from './useMedia-ewnkBszT.js';
import { C as ChevronDown, h as hideOthers, R as ReactRemoveScroll, u as useFocusGuards, F as FocusScope } from './index-z1Zuky25.js';

/**
 * @license lucide-react v0.562.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */


const __iconNode$1 = [
  ["path", { d: "M8 2v4", key: "1cmpym" }],
  ["path", { d: "M16 2v4", key: "4m81vk" }],
  ["rect", { width: "18", height: "18", x: "3", y: "4", rx: "2", key: "1hopcy" }],
  ["path", { d: "M3 10h18", key: "8toen8" }]
];
const Calendar$1 = createLucideIcon("calendar", __iconNode$1);

/**
 * @license lucide-react v0.562.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */


const __iconNode = [
  ["path", { d: "m21 21-4.34-4.34", key: "14j7rj" }],
  ["circle", { cx: "11", cy: "11", r: "8", key: "4ej97u" }]
];
const Search = createLucideIcon("search", __iconNode);

/**
 * Time zone name format.
 */

/**
 * The function returns the time zone name for the given date in the specified
 * time zone.
 *
 * It uses the `Intl.DateTimeFormat` API and by default outputs the time zone
 * name in a long format, e.g. "Pacific Standard Time" or
 * "Singapore Standard Time".
 *
 * It is possible to specify the format as the third argument using one of the following options
 *
 * - "short": e.g. "EDT" or "GMT+8".
 * - "long": e.g. "Eastern Daylight Time".
 * - "shortGeneric": e.g. "ET" or "Singapore Time".
 * - "longGeneric": e.g. "Eastern Time" or "Singapore Standard Time".
 *
 * These options correspond to TR35 tokens `z..zzz`, `zzzz`, `v`, and `vvvv` respectively: https://www.unicode.org/reports/tr35/tr35-dates.html#dfst-zone
 *
 * @param timeZone - Time zone name (IANA or UTC offset)
 * @param date - Date object to get the time zone name for
 * @param format - Optional format of the time zone name. Defaults to "long". Can be "short", "long", "shortGeneric", or "longGeneric".
 *
 * @returns Time zone name (e.g. "Singapore Standard Time")
 */
function tzName(timeZone, date, format = "long") {
  return new Intl.DateTimeFormat("en-US", {
    // Enforces engine to render the time. Without the option JavaScriptCore omits it.
    hour: "numeric",
    timeZone: timeZone,
    timeZoneName: format
  }).format(date).split(/\s/g) // Format.JS uses non-breaking spaces
  .slice(2) // Skip the hour and AM/PM parts
  .join(" ");
}

const offsetFormatCache = {};
const offsetCache = {};

/**
 * The function extracts UTC offset in minutes from the given date in specified
 * time zone.
 *
 * Unlike `Date.prototype.getTimezoneOffset`, this function returns the value
 * mirrored to the sign of the offset in the time zone. For Asia/Singapore
 * (UTC+8), `tzOffset` returns 480, while `getTimezoneOffset` returns -480.
 *
 * @param timeZone - Time zone name (IANA or UTC offset)
 * @param date - Date to check the offset for
 *
 * @returns UTC offset in minutes
 */
function tzOffset(timeZone, date) {
  try {
    const format = offsetFormatCache[timeZone] ||= new Intl.DateTimeFormat("en-US", {
      timeZone,
      timeZoneName: "longOffset"
    }).format;
    const offsetStr = format(date).split("GMT")[1];
    if (offsetStr in offsetCache) return offsetCache[offsetStr];
    return calcOffset(offsetStr, offsetStr.split(":"));
  } catch {
    // Fallback to manual parsing if the runtime doesn't support ±HH:MM/±HHMM/±HH
    // See: https://github.com/nodejs/node/issues/53419
    if (timeZone in offsetCache) return offsetCache[timeZone];
    const captures = timeZone?.match(offsetRe);
    if (captures) return calcOffset(timeZone, captures.slice(1));
    return NaN;
  }
}
const offsetRe = /([+-]\d\d):?(\d\d)?/;
function calcOffset(cacheStr, values) {
  const hours = +(values[0] || 0);
  const minutes = +(values[1] || 0);
  // Convert seconds to minutes by dividing by 60 to keep the function return in minutes.
  const seconds = +(values[2] || 0) / 60;
  return offsetCache[cacheStr] = hours * 60 + minutes > 0 ? hours * 60 + minutes + seconds : hours * 60 - minutes - seconds;
}

class TZDateMini extends Date {
  //#region static

  constructor(...args) {
    super();
    if (args.length > 1 && typeof args[args.length - 1] === "string") {
      this.timeZone = args.pop();
    }
    this.internal = new Date();
    if (isNaN(tzOffset(this.timeZone, this))) {
      this.setTime(NaN);
    } else {
      if (!args.length) {
        this.setTime(Date.now());
      } else if (typeof args[0] === "number" && (args.length === 1 || args.length === 2 && typeof args[1] !== "number")) {
        this.setTime(args[0]);
      } else if (typeof args[0] === "string") {
        this.setTime(+new Date(args[0]));
      } else if (args[0] instanceof Date) {
        this.setTime(+args[0]);
      } else {
        this.setTime(+new Date(...args));
        adjustToSystemTZ(this);
        syncToInternal(this);
      }
    }
  }
  static tz(tz, ...args) {
    return args.length ? new TZDateMini(...args, tz) : new TZDateMini(Date.now(), tz);
  }

  //#endregion

  //#region time zone

  withTimeZone(timeZone) {
    return new TZDateMini(+this, timeZone);
  }
  getTimezoneOffset() {
    const offset = -tzOffset(this.timeZone, this);
    // Remove the seconds offset
    // use Math.floor for negative GMT timezones and Math.ceil for positive GMT timezones.
    return offset > 0 ? Math.floor(offset) : Math.ceil(offset);
  }

  //#endregion

  //#region time

  setTime(time) {
    Date.prototype.setTime.apply(this, arguments);
    syncToInternal(this);
    return +this;
  }

  //#endregion

  //#region date-fns integration

  [Symbol.for("constructDateFrom")](date) {
    return new TZDateMini(+new Date(date), this.timeZone);
  }

  //#endregion
}

// Assign getters and setters
const re = /^(get|set)(?!UTC)/;
Object.getOwnPropertyNames(Date.prototype).forEach(method => {
  if (!re.test(method)) return;
  const utcMethod = method.replace(re, "$1UTC");
  // Filter out methods without UTC counterparts
  if (!TZDateMini.prototype[utcMethod]) return;
  if (method.startsWith("get")) {
    // Delegate to internal date's UTC method
    TZDateMini.prototype[method] = function () {
      return this.internal[utcMethod]();
    };
  } else {
    // Assign regular setter
    TZDateMini.prototype[method] = function () {
      Date.prototype[utcMethod].apply(this.internal, arguments);
      syncFromInternal(this);
      return +this;
    };

    // Assign UTC setter
    TZDateMini.prototype[utcMethod] = function () {
      Date.prototype[utcMethod].apply(this, arguments);
      syncToInternal(this);
      return +this;
    };
  }
});

/**
 * Function syncs time to internal date, applying the time zone offset.
 *
 * @param {Date} date - Date to sync
 */
function syncToInternal(date) {
  date.internal.setTime(+date);
  date.internal.setUTCSeconds(date.internal.getUTCSeconds() - Math.round(-tzOffset(date.timeZone, date) * 60));
}

/**
 * Function syncs the internal date UTC values to the date. It allows to get
 * accurate timestamp value.
 *
 * @param {Date} date - The date to sync
 */
function syncFromInternal(date) {
  // First we transpose the internal values
  Date.prototype.setFullYear.call(date, date.internal.getUTCFullYear(), date.internal.getUTCMonth(), date.internal.getUTCDate());
  Date.prototype.setHours.call(date, date.internal.getUTCHours(), date.internal.getUTCMinutes(), date.internal.getUTCSeconds(), date.internal.getUTCMilliseconds());

  // Now we have to adjust the date to the system time zone
  adjustToSystemTZ(date);
}

/**
 * Function adjusts the date to the system time zone. It uses the time zone
 * differences to calculate the offset and adjust the date.
 *
 * @param {Date} date - Date to adjust
 */
function adjustToSystemTZ(date) {
  // Save the time zone offset before all the adjustments
  const baseOffset = tzOffset(date.timeZone, date);
  // Remove the seconds offset
  // use Math.floor for negative GMT timezones and Math.ceil for positive GMT timezones.
  const offset = baseOffset > 0 ? Math.floor(baseOffset) : Math.ceil(baseOffset);
  //#region System DST adjustment

  // The biggest problem with using the system time zone is that when we create
  // a date from internal values stored in UTC, the system time zone might end
  // up on the DST hour:
  //
  //   $ TZ=America/New_York node
  //   > new Date(2020, 2, 8, 1).toString()
  //   'Sun Mar 08 2020 01:00:00 GMT-0500 (Eastern Standard Time)'
  //   > new Date(2020, 2, 8, 2).toString()
  //   'Sun Mar 08 2020 03:00:00 GMT-0400 (Eastern Daylight Time)'
  //   > new Date(2020, 2, 8, 3).toString()
  //   'Sun Mar 08 2020 03:00:00 GMT-0400 (Eastern Daylight Time)'
  //   > new Date(2020, 2, 8, 4).toString()
  //   'Sun Mar 08 2020 04:00:00 GMT-0400 (Eastern Daylight Time)'
  //
  // Here we get the same hour for both 2 and 3, because the system time zone
  // has DST beginning at 8 March 2020, 2 a.m. and jumps to 3 a.m. So we have
  // to adjust the internal date to reflect that.
  //
  // However we want to adjust only if that's the DST hour the change happenes,
  // not the hour where DST moves to.

  // We calculate the previous hour to see if the time zone offset has changed
  // and we have landed on the DST hour.
  const prevHour = new Date(+date);
  // We use UTC methods here as we don't want to land on the same hour again
  // in case of DST.
  prevHour.setUTCHours(prevHour.getUTCHours() - 1);

  // Calculate if we are on the system DST hour.
  const systemOffset = -new Date(+date).getTimezoneOffset();
  const prevHourSystemOffset = -new Date(+prevHour).getTimezoneOffset();
  const systemDSTChange = systemOffset - prevHourSystemOffset;
  // Detect the DST shift. System DST change will occur both on
  const dstShift = Date.prototype.getHours.apply(date) !== date.internal.getUTCHours();

  // Move the internal date when we are on the system DST hour.
  if (systemDSTChange && dstShift) date.internal.setUTCMinutes(date.internal.getUTCMinutes() + systemDSTChange);

  //#endregion

  //#region System diff adjustment

  // Now we need to adjust the date, since we just applied internal values.
  // We need to calculate the difference between the system and date time zones
  // and apply it to the date.

  const offsetDiff = systemOffset - offset;
  if (offsetDiff) Date.prototype.setUTCMinutes.call(date, Date.prototype.getUTCMinutes.call(date) + offsetDiff);

  //#endregion

  //#region Seconds System diff adjustment

  const systemDate = new Date(+date);
  // Set the UTC seconds to 0 to isolate the timezone offset in seconds.
  systemDate.setUTCSeconds(0);
  // For negative systemOffset, invert the seconds.
  const systemSecondsOffset = systemOffset > 0 ? systemDate.getSeconds() : (systemDate.getSeconds() - 60) % 60;

  // Calculate the seconds offset based on the timezone offset.
  const secondsOffset = Math.round(-(tzOffset(date.timeZone, date) * 60)) % 60;
  if (secondsOffset || systemSecondsOffset) {
    date.internal.setUTCSeconds(date.internal.getUTCSeconds() + secondsOffset);
    Date.prototype.setUTCSeconds.call(date, Date.prototype.getUTCSeconds.call(date) + secondsOffset + systemSecondsOffset);
  }

  //#endregion

  //#region Post-adjustment DST fix

  const postBaseOffset = tzOffset(date.timeZone, date);
  // Remove the seconds offset
  // use Math.floor for negative GMT timezones and Math.ceil for positive GMT timezones.
  const postOffset = postBaseOffset > 0 ? Math.floor(postBaseOffset) : Math.ceil(postBaseOffset);
  const postSystemOffset = -new Date(+date).getTimezoneOffset();
  const postOffsetDiff = postSystemOffset - postOffset;
  const offsetChanged = postOffset !== offset;
  const postDiff = postOffsetDiff - offsetDiff;
  if (offsetChanged && postDiff) {
    Date.prototype.setUTCMinutes.call(date, Date.prototype.getUTCMinutes.call(date) + postDiff);

    // Now we need to check if got offset change during the post-adjustment.
    // If so, we also need both dates to reflect that.

    const newBaseOffset = tzOffset(date.timeZone, date);
    // Remove the seconds offset
    // use Math.floor for negative GMT timezones and Math.ceil for positive GMT timezones.
    const newOffset = newBaseOffset > 0 ? Math.floor(newBaseOffset) : Math.ceil(newBaseOffset);
    const offsetChange = postOffset - newOffset;
    if (offsetChange) {
      date.internal.setUTCMinutes(date.internal.getUTCMinutes() + offsetChange);
      Date.prototype.setUTCMinutes.call(date, Date.prototype.getUTCMinutes.call(date) + offsetChange);
    }
  }

  //#endregion
}

class TZDate extends TZDateMini {
  //#region static

  static tz(tz, ...args) {
    return args.length ? new TZDate(...args, tz) : new TZDate(Date.now(), tz);
  }

  //#endregion

  //#region representation

  toISOString() {
    const [sign, hours, minutes] = this.tzComponents();
    const tz = `${sign}${hours}:${minutes}`;
    return this.internal.toISOString().slice(0, -1) + tz;
  }
  toString() {
    // "Tue Aug 13 2024 07:50:19 GMT+0800 (Singapore Standard Time)";
    return `${this.toDateString()} ${this.toTimeString()}`;
  }
  toDateString() {
    // toUTCString returns RFC 7231 ("Mon, 12 Aug 2024 23:36:08 GMT")
    const [day, date, month, year] = this.internal.toUTCString().split(" ");
    // "Tue Aug 13 2024"
    return `${day?.slice(0, -1) /* Remove "," */} ${month} ${date} ${year}`;
  }
  toTimeString() {
    // toUTCString returns RFC 7231 ("Mon, 12 Aug 2024 23:36:08 GMT")
    const time = this.internal.toUTCString().split(" ")[4];
    const [sign, hours, minutes] = this.tzComponents();
    // "07:42:23 GMT+0800 (Singapore Standard Time)"
    return `${time} GMT${sign}${hours}${minutes} (${tzName(this.timeZone, this)})`;
  }
  toLocaleString(locales, options) {
    return Date.prototype.toLocaleString.call(this, locales, {
      ...options,
      timeZone: options?.timeZone || this.timeZone
    });
  }
  toLocaleDateString(locales, options) {
    return Date.prototype.toLocaleDateString.call(this, locales, {
      ...options,
      timeZone: options?.timeZone || this.timeZone
    });
  }
  toLocaleTimeString(locales, options) {
    return Date.prototype.toLocaleTimeString.call(this, locales, {
      ...options,
      timeZone: options?.timeZone || this.timeZone
    });
  }

  //#endregion

  //#region private

  tzComponents() {
    const offset = this.getTimezoneOffset();
    const sign = offset > 0 ? "-" : "+";
    const hours = String(Math.floor(Math.abs(offset) / 60)).padStart(2, "0");
    const minutes = String(Math.abs(offset) % 60).padStart(2, "0");
    return [sign, hours, minutes];
  }

  //#endregion

  withTimeZone(timeZone) {
    return new TZDate(+this, timeZone);
  }

  //#region date-fns integration

  [Symbol.for("constructDateFrom")](date) {
    return new TZDate(+new Date(date), this.timeZone);
  }

  //#endregion
}

/**
 * @module constants
 * @summary Useful constants
 * @description
 * Collection of useful date constants.
 *
 * The constants could be imported from `date-fns/constants`:
 *
 * ```ts
 * import { maxTime, minTime } from "./constants/date-fns/constants";
 *
 * function isAllowedTime(time) {
 *   return time <= maxTime && time >= minTime;
 * }
 * ```
 */


/**
 * @constant
 * @name millisecondsInWeek
 * @summary Milliseconds in 1 week.
 */
const millisecondsInWeek = 604800000;

/**
 * @constant
 * @name millisecondsInDay
 * @summary Milliseconds in 1 day.
 */
const millisecondsInDay = 86400000;

/**
 * @constant
 * @name constructFromSymbol
 * @summary Symbol enabling Date extensions to inherit properties from the reference date.
 *
 * The symbol is used to enable the `constructFrom` function to construct a date
 * using a reference date and a value. It allows to transfer extra properties
 * from the reference date to the new date. It's useful for extensions like
 * [`TZDate`](https://github.com/date-fns/tz) that accept a time zone as
 * a constructor argument.
 */
const constructFromSymbol = Symbol.for("constructDateFrom");

/**
 * @name constructFrom
 * @category Generic Helpers
 * @summary Constructs a date using the reference date and the value
 *
 * @description
 * The function constructs a new date using the constructor from the reference
 * date and the given value. It helps to build generic functions that accept
 * date extensions.
 *
 * It defaults to `Date` if the passed reference date is a number or a string.
 *
 * Starting from v3.7.0, it allows to construct a date using `[Symbol.for("constructDateFrom")]`
 * enabling to transfer extra properties from the reference date to the new date.
 * It's useful for extensions like [`TZDate`](https://github.com/date-fns/tz)
 * that accept a time zone as a constructor argument.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 *
 * @param date - The reference date to take constructor from
 * @param value - The value to create the date
 *
 * @returns Date initialized using the given date and value
 *
 * @example
 * import { constructFrom } from "./constructFrom/date-fns";
 *
 * // A function that clones a date preserving the original type
 * function cloneDate<DateType extends Date>(date: DateType): DateType {
 *   return constructFrom(
 *     date, // Use constructor from the given date
 *     date.getTime() // Use the date value to create a new date
 *   );
 * }
 */
function constructFrom(date, value) {
  if (typeof date === "function") return date(value);

  if (date && typeof date === "object" && constructFromSymbol in date)
    return date[constructFromSymbol](value);

  if (date instanceof Date) return new date.constructor(value);

  return new Date(value);
}

/**
 * @name toDate
 * @category Common Helpers
 * @summary Convert the given argument to an instance of Date.
 *
 * @description
 * Convert the given argument to an instance of Date.
 *
 * If the argument is an instance of Date, the function returns its clone.
 *
 * If the argument is a number, it is treated as a timestamp.
 *
 * If the argument is none of the above, the function returns Invalid Date.
 *
 * Starting from v3.7.0, it clones a date using `[Symbol.for("constructDateFrom")]`
 * enabling to transfer extra properties from the reference date to the new date.
 * It's useful for extensions like [`TZDate`](https://github.com/date-fns/tz)
 * that accept a time zone as a constructor argument.
 *
 * **Note**: *all* Date arguments passed to any *date-fns* function is processed by `toDate`.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param argument - The value to convert
 *
 * @returns The parsed date in the local time zone
 *
 * @example
 * // Clone the date:
 * const result = toDate(new Date(2014, 1, 11, 11, 30, 30))
 * //=> Tue Feb 11 2014 11:30:30
 *
 * @example
 * // Convert the timestamp to date:
 * const result = toDate(1392098430000)
 * //=> Tue Feb 11 2014 11:30:30
 */
function toDate(argument, context) {
  // [TODO] Get rid of `toDate` or `constructFrom`?
  return constructFrom(context || argument, argument);
}

/**
 * The {@link addDays} function options.
 */

/**
 * @name addDays
 * @category Day Helpers
 * @summary Add the specified number of days to the given date.
 *
 * @description
 * Add the specified number of days to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The date to be changed
 * @param amount - The amount of days to be added.
 * @param options - An object with options
 *
 * @returns The new date with the days added
 *
 * @example
 * // Add 10 days to 1 September 2014:
 * const result = addDays(new Date(2014, 8, 1), 10)
 * //=> Thu Sep 11 2014 00:00:00
 */
function addDays(date, amount, options) {
  const _date = toDate(date, options?.in);
  if (isNaN(amount)) return constructFrom(date, NaN);

  // If 0 days, no-op to avoid changing times in the hour before end of DST
  if (!amount) return _date;

  _date.setDate(_date.getDate() + amount);
  return _date;
}

/**
 * The {@link addMonths} function options.
 */

/**
 * @name addMonths
 * @category Month Helpers
 * @summary Add the specified number of months to the given date.
 *
 * @description
 * Add the specified number of months to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The date to be changed
 * @param amount - The amount of months to be added.
 * @param options - The options object
 *
 * @returns The new date with the months added
 *
 * @example
 * // Add 5 months to 1 September 2014:
 * const result = addMonths(new Date(2014, 8, 1), 5)
 * //=> Sun Feb 01 2015 00:00:00
 *
 * // Add one month to 30 January 2023:
 * const result = addMonths(new Date(2023, 0, 30), 1)
 * //=> Tue Feb 28 2023 00:00:00
 */
function addMonths(date, amount, options) {
  const _date = toDate(date, options?.in);
  if (isNaN(amount)) return constructFrom(date, NaN);
  if (!amount) {
    // If 0 months, no-op to avoid changing times in the hour before end of DST
    return _date;
  }
  const dayOfMonth = _date.getDate();

  // The JS Date object supports date math by accepting out-of-bounds values for
  // month, day, etc. For example, new Date(2020, 0, 0) returns 31 Dec 2019 and
  // new Date(2020, 13, 1) returns 1 Feb 2021.  This is *almost* the behavior we
  // want except that dates will wrap around the end of a month, meaning that
  // new Date(2020, 13, 31) will return 3 Mar 2021 not 28 Feb 2021 as desired. So
  // we'll default to the end of the desired month by adding 1 to the desired
  // month and using a date of 0 to back up one day to the end of the desired
  // month.
  const endOfDesiredMonth = constructFrom(date, _date.getTime());
  endOfDesiredMonth.setMonth(_date.getMonth() + amount + 1, 0);
  const daysInMonth = endOfDesiredMonth.getDate();
  if (dayOfMonth >= daysInMonth) {
    // If we're already at the end of the month, then this is the correct date
    // and we're done.
    return endOfDesiredMonth;
  } else {
    // Otherwise, we now know that setting the original day-of-month value won't
    // cause an overflow, so set the desired day-of-month. Note that we can't
    // just set the date of `endOfDesiredMonth` because that object may have had
    // its time changed in the unusual case where where a DST transition was on
    // the last day of the month and its local time was in the hour skipped or
    // repeated next to a DST transition.  So we use `date` instead which is
    // guaranteed to still have the original time.
    _date.setFullYear(
      endOfDesiredMonth.getFullYear(),
      endOfDesiredMonth.getMonth(),
      dayOfMonth,
    );
    return _date;
  }
}

let defaultOptions = {};

function getDefaultOptions() {
  return defaultOptions;
}

/**
 * The {@link startOfWeek} function options.
 */

/**
 * @name startOfWeek
 * @category Week Helpers
 * @summary Return the start of a week for the given date.
 *
 * @description
 * Return the start of a week for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The start of a week
 *
 * @example
 * // The start of a week for 2 September 2014 11:55:00:
 * const result = startOfWeek(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Sun Aug 31 2014 00:00:00
 *
 * @example
 * // If the week starts on Monday, the start of the week for 2 September 2014 11:55:00:
 * const result = startOfWeek(new Date(2014, 8, 2, 11, 55, 0), { weekStartsOn: 1 })
 * //=> Mon Sep 01 2014 00:00:00
 */
function startOfWeek(date, options) {
  const defaultOptions = getDefaultOptions();
  const weekStartsOn =
    options?.weekStartsOn ??
    options?.locale?.options?.weekStartsOn ??
    defaultOptions.weekStartsOn ??
    defaultOptions.locale?.options?.weekStartsOn ??
    0;

  const _date = toDate(date, options?.in);
  const day = _date.getDay();
  const diff = (day < weekStartsOn ? 7 : 0) + day - weekStartsOn;

  _date.setDate(_date.getDate() - diff);
  _date.setHours(0, 0, 0, 0);
  return _date;
}

/**
 * The {@link startOfISOWeek} function options.
 */

/**
 * @name startOfISOWeek
 * @category ISO Week Helpers
 * @summary Return the start of an ISO week for the given date.
 *
 * @description
 * Return the start of an ISO week for the given date.
 * The result will be in the local timezone.
 *
 * ISO week-numbering year: http://en.wikipedia.org/wiki/ISO_week_date
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The start of an ISO week
 *
 * @example
 * // The start of an ISO week for 2 September 2014 11:55:00:
 * const result = startOfISOWeek(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Mon Sep 01 2014 00:00:00
 */
function startOfISOWeek(date, options) {
  return startOfWeek(date, { ...options, weekStartsOn: 1 });
}

/**
 * The {@link getISOWeekYear} function options.
 */

/**
 * @name getISOWeekYear
 * @category ISO Week-Numbering Year Helpers
 * @summary Get the ISO week-numbering year of the given date.
 *
 * @description
 * Get the ISO week-numbering year of the given date,
 * which always starts 3 days before the year's first Thursday.
 *
 * ISO week-numbering year: http://en.wikipedia.org/wiki/ISO_week_date
 *
 * @param date - The given date
 *
 * @returns The ISO week-numbering year
 *
 * @example
 * // Which ISO-week numbering year is 2 January 2005?
 * const result = getISOWeekYear(new Date(2005, 0, 2))
 * //=> 2004
 */
function getISOWeekYear(date, options) {
  const _date = toDate(date, options?.in);
  const year = _date.getFullYear();

  const fourthOfJanuaryOfNextYear = constructFrom(_date, 0);
  fourthOfJanuaryOfNextYear.setFullYear(year + 1, 0, 4);
  fourthOfJanuaryOfNextYear.setHours(0, 0, 0, 0);
  const startOfNextYear = startOfISOWeek(fourthOfJanuaryOfNextYear);

  const fourthOfJanuaryOfThisYear = constructFrom(_date, 0);
  fourthOfJanuaryOfThisYear.setFullYear(year, 0, 4);
  fourthOfJanuaryOfThisYear.setHours(0, 0, 0, 0);
  const startOfThisYear = startOfISOWeek(fourthOfJanuaryOfThisYear);

  if (_date.getTime() >= startOfNextYear.getTime()) {
    return year + 1;
  } else if (_date.getTime() >= startOfThisYear.getTime()) {
    return year;
  } else {
    return year - 1;
  }
}

/**
 * Google Chrome as of 67.0.3396.87 introduced timezones with offset that includes seconds.
 * They usually appear for dates that denote time before the timezones were introduced
 * (e.g. for 'Europe/Prague' timezone the offset is GMT+00:57:44 before 1 October 1891
 * and GMT+01:00:00 after that date)
 *
 * Date#getTimezoneOffset returns the offset in minutes and would return 57 for the example above,
 * which would lead to incorrect calculations.
 *
 * This function returns the timezone offset in milliseconds that takes seconds in account.
 */
function getTimezoneOffsetInMilliseconds(date) {
  const _date = toDate(date);
  const utcDate = new Date(
    Date.UTC(
      _date.getFullYear(),
      _date.getMonth(),
      _date.getDate(),
      _date.getHours(),
      _date.getMinutes(),
      _date.getSeconds(),
      _date.getMilliseconds(),
    ),
  );
  utcDate.setUTCFullYear(_date.getFullYear());
  return +date - +utcDate;
}

function normalizeDates(context, ...dates) {
  const normalize = constructFrom.bind(
    null,
    dates.find((date) => typeof date === "object"),
  );
  return dates.map(normalize);
}

/**
 * The {@link startOfDay} function options.
 */

/**
 * @name startOfDay
 * @category Day Helpers
 * @summary Return the start of a day for the given date.
 *
 * @description
 * Return the start of a day for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - The options
 *
 * @returns The start of a day
 *
 * @example
 * // The start of a day for 2 September 2014 11:55:00:
 * const result = startOfDay(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Tue Sep 02 2014 00:00:00
 */
function startOfDay(date, options) {
  const _date = toDate(date, options?.in);
  _date.setHours(0, 0, 0, 0);
  return _date;
}

/**
 * The {@link differenceInCalendarDays} function options.
 */

/**
 * @name differenceInCalendarDays
 * @category Day Helpers
 * @summary Get the number of calendar days between the given dates.
 *
 * @description
 * Get the number of calendar days between the given dates. This means that the times are removed
 * from the dates and then the difference in days is calculated.
 *
 * @param laterDate - The later date
 * @param earlierDate - The earlier date
 * @param options - The options object
 *
 * @returns The number of calendar days
 *
 * @example
 * // How many calendar days are between
 * // 2 July 2011 23:00:00 and 2 July 2012 00:00:00?
 * const result = differenceInCalendarDays(
 *   new Date(2012, 6, 2, 0, 0),
 *   new Date(2011, 6, 2, 23, 0)
 * )
 * //=> 366
 * // How many calendar days are between
 * // 2 July 2011 23:59:00 and 3 July 2011 00:01:00?
 * const result = differenceInCalendarDays(
 *   new Date(2011, 6, 3, 0, 1),
 *   new Date(2011, 6, 2, 23, 59)
 * )
 * //=> 1
 */
function differenceInCalendarDays(laterDate, earlierDate, options) {
  const [laterDate_, earlierDate_] = normalizeDates(
    options?.in,
    laterDate,
    earlierDate,
  );

  const laterStartOfDay = startOfDay(laterDate_);
  const earlierStartOfDay = startOfDay(earlierDate_);

  const laterTimestamp =
    +laterStartOfDay - getTimezoneOffsetInMilliseconds(laterStartOfDay);
  const earlierTimestamp =
    +earlierStartOfDay - getTimezoneOffsetInMilliseconds(earlierStartOfDay);

  // Round the number of days to the nearest integer because the number of
  // milliseconds in a day is not constant (e.g. it's different in the week of
  // the daylight saving time clock shift).
  return Math.round((laterTimestamp - earlierTimestamp) / millisecondsInDay);
}

/**
 * The {@link startOfISOWeekYear} function options.
 */

/**
 * @name startOfISOWeekYear
 * @category ISO Week-Numbering Year Helpers
 * @summary Return the start of an ISO week-numbering year for the given date.
 *
 * @description
 * Return the start of an ISO week-numbering year,
 * which always starts 3 days before the year's first Thursday.
 * The result will be in the local timezone.
 *
 * ISO week-numbering year: http://en.wikipedia.org/wiki/ISO_week_date
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The start of an ISO week-numbering year
 *
 * @example
 * // The start of an ISO week-numbering year for 2 July 2005:
 * const result = startOfISOWeekYear(new Date(2005, 6, 2))
 * //=> Mon Jan 03 2005 00:00:00
 */
function startOfISOWeekYear(date, options) {
  const year = getISOWeekYear(date, options);
  const fourthOfJanuary = constructFrom(date, 0);
  fourthOfJanuary.setFullYear(year, 0, 4);
  fourthOfJanuary.setHours(0, 0, 0, 0);
  return startOfISOWeek(fourthOfJanuary);
}

/**
 * The {@link addWeeks} function options.
 */

/**
 * @name addWeeks
 * @category Week Helpers
 * @summary Add the specified number of weeks to the given date.
 *
 * @description
 * Add the specified number of weeks to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The date to be changed
 * @param amount - The amount of weeks to be added.
 * @param options - An object with options
 *
 * @returns The new date with the weeks added
 *
 * @example
 * // Add 4 weeks to 1 September 2014:
 * const result = addWeeks(new Date(2014, 8, 1), 4)
 * //=> Mon Sep 29 2014 00:00:00
 */
function addWeeks(date, amount, options) {
  return addDays(date, amount * 7, options);
}

/**
 * The {@link addYears} function options.
 */

/**
 * @name addYears
 * @category Year Helpers
 * @summary Add the specified number of years to the given date.
 *
 * @description
 * Add the specified number of years to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type.
 *
 * @param date - The date to be changed
 * @param amount - The amount of years to be added.
 * @param options - The options
 *
 * @returns The new date with the years added
 *
 * @example
 * // Add 5 years to 1 September 2014:
 * const result = addYears(new Date(2014, 8, 1), 5)
 * //=> Sun Sep 01 2019 00:00:00
 */
function addYears(date, amount, options) {
  return addMonths(date, amount * 12, options);
}

/**
 * The {@link max} function options.
 */

/**
 * @name max
 * @category Common Helpers
 * @summary Return the latest of the given dates.
 *
 * @description
 * Return the latest of the given dates.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param dates - The dates to compare
 *
 * @returns The latest of the dates
 *
 * @example
 * // Which of these dates is the latest?
 * const result = max([
 *   new Date(1989, 6, 10),
 *   new Date(1987, 1, 11),
 *   new Date(1995, 6, 2),
 *   new Date(1990, 0, 1)
 * ])
 * //=> Sun Jul 02 1995 00:00:00
 */
function max(dates, options) {
  let result;
  let context = options?.in;

  dates.forEach((date) => {
    // Use the first date object as the context function
    if (!context && typeof date === "object")
      context = constructFrom.bind(null, date);

    const date_ = toDate(date, context);
    if (!result || result < date_ || isNaN(+date_)) result = date_;
  });

  return constructFrom(context, result || NaN);
}

/**
 * The {@link min} function options.
 */

/**
 * @name min
 * @category Common Helpers
 * @summary Returns the earliest of the given dates.
 *
 * @description
 * Returns the earliest of the given dates.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param dates - The dates to compare
 *
 * @returns The earliest of the dates
 *
 * @example
 * // Which of these dates is the earliest?
 * const result = min([
 *   new Date(1989, 6, 10),
 *   new Date(1987, 1, 11),
 *   new Date(1995, 6, 2),
 *   new Date(1990, 0, 1)
 * ])
 * //=> Wed Feb 11 1987 00:00:00
 */
function min(dates, options) {
  let result;
  let context = options?.in;

  dates.forEach((date) => {
    // Use the first date object as the context function
    if (!context && typeof date === "object")
      context = constructFrom.bind(null, date);

    const date_ = toDate(date, context);
    if (!result || result > date_ || isNaN(+date_)) result = date_;
  });

  return constructFrom(context, result || NaN);
}

/**
 * The {@link isSameDay} function options.
 */

/**
 * @name isSameDay
 * @category Day Helpers
 * @summary Are the given dates in the same day (and year and month)?
 *
 * @description
 * Are the given dates in the same day (and year and month)?
 *
 * @param laterDate - The first date to check
 * @param earlierDate - The second date to check
 * @param options - An object with options
 *
 * @returns The dates are in the same day (and year and month)
 *
 * @example
 * // Are 4 September 06:00:00 and 4 September 18:00:00 in the same day?
 * const result = isSameDay(new Date(2014, 8, 4, 6, 0), new Date(2014, 8, 4, 18, 0))
 * //=> true
 *
 * @example
 * // Are 4 September and 4 October in the same day?
 * const result = isSameDay(new Date(2014, 8, 4), new Date(2014, 9, 4))
 * //=> false
 *
 * @example
 * // Are 4 September, 2014 and 4 September, 2015 in the same day?
 * const result = isSameDay(new Date(2014, 8, 4), new Date(2015, 8, 4))
 * //=> false
 */
function isSameDay(laterDate, earlierDate, options) {
  const [dateLeft_, dateRight_] = normalizeDates(
    options?.in,
    laterDate,
    earlierDate,
  );
  return +startOfDay(dateLeft_) === +startOfDay(dateRight_);
}

/**
 * @name isDate
 * @category Common Helpers
 * @summary Is the given value a date?
 *
 * @description
 * Returns true if the given value is an instance of Date. The function works for dates transferred across iframes.
 *
 * @param value - The value to check
 *
 * @returns True if the given value is a date
 *
 * @example
 * // For a valid date:
 * const result = isDate(new Date())
 * //=> true
 *
 * @example
 * // For an invalid date:
 * const result = isDate(new Date(NaN))
 * //=> true
 *
 * @example
 * // For some value:
 * const result = isDate('2014-02-31')
 * //=> false
 *
 * @example
 * // For an object:
 * const result = isDate({})
 * //=> false
 */
function isDate(value) {
  return (
    value instanceof Date ||
    (typeof value === "object" &&
      Object.prototype.toString.call(value) === "[object Date]")
  );
}

/**
 * @name isValid
 * @category Common Helpers
 * @summary Is the given date valid?
 *
 * @description
 * Returns false if argument is Invalid Date and true otherwise.
 * Argument is converted to Date using `toDate`. See [toDate](https://date-fns.org/docs/toDate)
 * Invalid Date is a Date, whose time value is NaN.
 *
 * Time value of Date: http://es5.github.io/#x15.9.1.1
 *
 * @param date - The date to check
 *
 * @returns The date is valid
 *
 * @example
 * // For the valid date:
 * const result = isValid(new Date(2014, 1, 31))
 * //=> true
 *
 * @example
 * // For the value, convertible into a date:
 * const result = isValid(1393804800000)
 * //=> true
 *
 * @example
 * // For the invalid date:
 * const result = isValid(new Date(''))
 * //=> false
 */
function isValid(date) {
  return !((!isDate(date) && typeof date !== "number") || isNaN(+toDate(date)));
}

/**
 * The {@link differenceInCalendarMonths} function options.
 */

/**
 * @name differenceInCalendarMonths
 * @category Month Helpers
 * @summary Get the number of calendar months between the given dates.
 *
 * @description
 * Get the number of calendar months between the given dates.
 *
 * @param laterDate - The later date
 * @param earlierDate - The earlier date
 * @param options - An object with options
 *
 * @returns The number of calendar months
 *
 * @example
 * // How many calendar months are between 31 January 2014 and 1 September 2014?
 * const result = differenceInCalendarMonths(
 *   new Date(2014, 8, 1),
 *   new Date(2014, 0, 31)
 * )
 * //=> 8
 */
function differenceInCalendarMonths(laterDate, earlierDate, options) {
  const [laterDate_, earlierDate_] = normalizeDates(
    options?.in,
    laterDate,
    earlierDate,
  );

  const yearsDiff = laterDate_.getFullYear() - earlierDate_.getFullYear();
  const monthsDiff = laterDate_.getMonth() - earlierDate_.getMonth();

  return yearsDiff * 12 + monthsDiff;
}

/**
 * The {@link endOfMonth} function options.
 */

/**
 * @name endOfMonth
 * @category Month Helpers
 * @summary Return the end of a month for the given date.
 *
 * @description
 * Return the end of a month for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The end of a month
 *
 * @example
 * // The end of a month for 2 September 2014 11:55:00:
 * const result = endOfMonth(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Tue Sep 30 2014 23:59:59.999
 */
function endOfMonth(date, options) {
  const _date = toDate(date, options?.in);
  const month = _date.getMonth();
  _date.setFullYear(_date.getFullYear(), month + 1, 0);
  _date.setHours(23, 59, 59, 999);
  return _date;
}

function normalizeInterval(context, interval) {
  const [start, end] = normalizeDates(context, interval.start, interval.end);
  return { start, end };
}

/**
 * The {@link eachMonthOfInterval} function options.
 */

/**
 * The {@link eachMonthOfInterval} function result type. It resolves the proper data type.
 */

/**
 * @name eachMonthOfInterval
 * @category Interval Helpers
 * @summary Return the array of months within the specified time interval.
 *
 * @description
 * Return the array of months within the specified time interval.
 *
 * @typeParam IntervalType - Interval type.
 * @typeParam Options - Options type.
 *
 * @param interval - The interval.
 * @param options - An object with options.
 *
 * @returns The array with starts of months from the month of the interval start to the month of the interval end
 *
 * @example
 * // Each month between 6 February 2014 and 10 August 2014:
 * const result = eachMonthOfInterval({
 *   start: new Date(2014, 1, 6),
 *   end: new Date(2014, 7, 10)
 * })
 * //=> [
 * //   Sat Feb 01 2014 00:00:00,
 * //   Sat Mar 01 2014 00:00:00,
 * //   Tue Apr 01 2014 00:00:00,
 * //   Thu May 01 2014 00:00:00,
 * //   Sun Jun 01 2014 00:00:00,
 * //   Tue Jul 01 2014 00:00:00,
 * //   Fri Aug 01 2014 00:00:00
 * // ]
 */
function eachMonthOfInterval(interval, options) {
  const { start, end } = normalizeInterval(options?.in, interval);

  let reversed = +start > +end;
  const endTime = reversed ? +start : +end;
  const date = reversed ? end : start;
  date.setHours(0, 0, 0, 0);
  date.setDate(1);

  let step = 1;

  const dates = [];

  while (+date <= endTime) {
    dates.push(constructFrom(start, date));
    date.setMonth(date.getMonth() + step);
  }

  return reversed ? dates.reverse() : dates;
}

/**
 * The {@link startOfMonth} function options.
 */

/**
 * @name startOfMonth
 * @category Month Helpers
 * @summary Return the start of a month for the given date.
 *
 * @description
 * Return the start of a month for the given date. The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments.
 * Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed,
 * or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The start of a month
 *
 * @example
 * // The start of a month for 2 September 2014 11:55:00:
 * const result = startOfMonth(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Mon Sep 01 2014 00:00:00
 */
function startOfMonth(date, options) {
  const _date = toDate(date, options?.in);
  _date.setDate(1);
  _date.setHours(0, 0, 0, 0);
  return _date;
}

/**
 * The {@link endOfYear} function options.
 */

/**
 * @name endOfYear
 * @category Year Helpers
 * @summary Return the end of a year for the given date.
 *
 * @description
 * Return the end of a year for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - The options
 *
 * @returns The end of a year
 *
 * @example
 * // The end of a year for 2 September 2014 11:55:00:
 * const result = endOfYear(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Wed Dec 31 2014 23:59:59.999
 */
function endOfYear(date, options) {
  const _date = toDate(date, options?.in);
  const year = _date.getFullYear();
  _date.setFullYear(year + 1, 0, 0);
  _date.setHours(23, 59, 59, 999);
  return _date;
}

/**
 * The {@link startOfYear} function options.
 */

/**
 * @name startOfYear
 * @category Year Helpers
 * @summary Return the start of a year for the given date.
 *
 * @description
 * Return the start of a year for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - The options
 *
 * @returns The start of a year
 *
 * @example
 * // The start of a year for 2 September 2014 11:55:00:
 * const result = startOfYear(new Date(2014, 8, 2, 11, 55, 00))
 * //=> Wed Jan 01 2014 00:00:00
 */
function startOfYear(date, options) {
  const date_ = toDate(date, options?.in);
  date_.setFullYear(date_.getFullYear(), 0, 1);
  date_.setHours(0, 0, 0, 0);
  return date_;
}

/**
 * The {@link eachYearOfInterval} function options.
 */

/**
 * The {@link eachYearOfInterval} function result type. It resolves the proper data type.
 * It uses the first argument date object type, starting from the date argument,
 * then the start interval date, and finally the end interval date. If
 * a context function is passed, it uses the context function return type.
 */

/**
 * @name eachYearOfInterval
 * @category Interval Helpers
 * @summary Return the array of yearly timestamps within the specified time interval.
 *
 * @description
 * Return the array of yearly timestamps within the specified time interval.
 *
 * @typeParam IntervalType - Interval type.
 * @typeParam Options - Options type.
 *
 * @param interval - The interval.
 * @param options - An object with options.
 *
 * @returns The array with starts of yearly timestamps from the month of the interval start to the month of the interval end
 *
 * @example
 * // Each year between 6 February 2014 and 10 August 2017:
 * const result = eachYearOfInterval({
 *   start: new Date(2014, 1, 6),
 *   end: new Date(2017, 7, 10)
 * })
 * //=> [
 * //   Wed Jan 01 2014 00:00:00,
 * //   Thu Jan 01 2015 00:00:00,
 * //   Fri Jan 01 2016 00:00:00,
 * //   Sun Jan 01 2017 00:00:00
 * // ]
 */
function eachYearOfInterval(interval, options) {
  const { start, end } = normalizeInterval(options?.in, interval);

  let reversed = +start > +end;
  const endTime = reversed ? +start : +end;
  const date = reversed ? end : start;
  date.setHours(0, 0, 0, 0);
  date.setMonth(0, 1);

  let step = 1;

  const dates = [];

  while (+date <= endTime) {
    dates.push(constructFrom(start, date));
    date.setFullYear(date.getFullYear() + step);
  }

  return reversed ? dates.reverse() : dates;
}

/**
 * The {@link endOfWeek} function options.
 */

/**
 * @name endOfWeek
 * @category Week Helpers
 * @summary Return the end of a week for the given date.
 *
 * @description
 * Return the end of a week for the given date.
 * The result will be in the local timezone.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The end of a week
 *
 * @example
 * // The end of a week for 2 September 2014 11:55:00:
 * const result = endOfWeek(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Sat Sep 06 2014 23:59:59.999
 *
 * @example
 * // If the week starts on Monday, the end of the week for 2 September 2014 11:55:00:
 * const result = endOfWeek(new Date(2014, 8, 2, 11, 55, 0), { weekStartsOn: 1 })
 * //=> Sun Sep 07 2014 23:59:59.999
 */
function endOfWeek(date, options) {
  const defaultOptions = getDefaultOptions();
  const weekStartsOn =
    options?.weekStartsOn ??
    options?.locale?.options?.weekStartsOn ??
    defaultOptions.weekStartsOn ??
    defaultOptions.locale?.options?.weekStartsOn ??
    0;

  const _date = toDate(date, options?.in);
  const day = _date.getDay();
  const diff = (day < weekStartsOn ? -7 : 0) + 6 - (day - weekStartsOn);

  _date.setDate(_date.getDate() + diff);
  _date.setHours(23, 59, 59, 999);
  return _date;
}

/**
 * The {@link endOfISOWeek} function options.
 */

/**
 * @name endOfISOWeek
 * @category ISO Week Helpers
 * @summary Return the end of an ISO week for the given date.
 *
 * @description
 * Return the end of an ISO week for the given date.
 * The result will be in the local timezone.
 *
 * ISO week-numbering year: http://en.wikipedia.org/wiki/ISO_week_date
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The end of an ISO week
 *
 * @example
 * // The end of an ISO week for 2 September 2014 11:55:00:
 * const result = endOfISOWeek(new Date(2014, 8, 2, 11, 55, 0))
 * //=> Sun Sep 07 2014 23:59:59.999
 */
function endOfISOWeek(date, options) {
  return endOfWeek(date, { ...options, weekStartsOn: 1 });
}

const formatDistanceLocale = {
  lessThanXSeconds: {
    one: "less than a second",
    other: "less than {{count}} seconds",
  },

  xSeconds: {
    one: "1 second",
    other: "{{count}} seconds",
  },

  halfAMinute: "half a minute",

  lessThanXMinutes: {
    one: "less than a minute",
    other: "less than {{count}} minutes",
  },

  xMinutes: {
    one: "1 minute",
    other: "{{count}} minutes",
  },

  aboutXHours: {
    one: "about 1 hour",
    other: "about {{count}} hours",
  },

  xHours: {
    one: "1 hour",
    other: "{{count}} hours",
  },

  xDays: {
    one: "1 day",
    other: "{{count}} days",
  },

  aboutXWeeks: {
    one: "about 1 week",
    other: "about {{count}} weeks",
  },

  xWeeks: {
    one: "1 week",
    other: "{{count}} weeks",
  },

  aboutXMonths: {
    one: "about 1 month",
    other: "about {{count}} months",
  },

  xMonths: {
    one: "1 month",
    other: "{{count}} months",
  },

  aboutXYears: {
    one: "about 1 year",
    other: "about {{count}} years",
  },

  xYears: {
    one: "1 year",
    other: "{{count}} years",
  },

  overXYears: {
    one: "over 1 year",
    other: "over {{count}} years",
  },

  almostXYears: {
    one: "almost 1 year",
    other: "almost {{count}} years",
  },
};

const formatDistance = (token, count, options) => {
  let result;

  const tokenValue = formatDistanceLocale[token];
  if (typeof tokenValue === "string") {
    result = tokenValue;
  } else if (count === 1) {
    result = tokenValue.one;
  } else {
    result = tokenValue.other.replace("{{count}}", count.toString());
  }

  if (options?.addSuffix) {
    if (options.comparison && options.comparison > 0) {
      return "in " + result;
    } else {
      return result + " ago";
    }
  }

  return result;
};

function buildFormatLongFn(args) {
  return (options = {}) => {
    // TODO: Remove String()
    const width = options.width ? String(options.width) : args.defaultWidth;
    const format = args.formats[width] || args.formats[args.defaultWidth];
    return format;
  };
}

const dateFormats = {
  full: "EEEE, MMMM do, y",
  long: "MMMM do, y",
  medium: "MMM d, y",
  short: "MM/dd/yyyy",
};

const timeFormats = {
  full: "h:mm:ss a zzzz",
  long: "h:mm:ss a z",
  medium: "h:mm:ss a",
  short: "h:mm a",
};

const dateTimeFormats = {
  full: "{{date}} 'at' {{time}}",
  long: "{{date}} 'at' {{time}}",
  medium: "{{date}}, {{time}}",
  short: "{{date}}, {{time}}",
};

const formatLong = {
  date: buildFormatLongFn({
    formats: dateFormats,
    defaultWidth: "full",
  }),

  time: buildFormatLongFn({
    formats: timeFormats,
    defaultWidth: "full",
  }),

  dateTime: buildFormatLongFn({
    formats: dateTimeFormats,
    defaultWidth: "full",
  }),
};

const formatRelativeLocale = {
  lastWeek: "'last' eeee 'at' p",
  yesterday: "'yesterday at' p",
  today: "'today at' p",
  tomorrow: "'tomorrow at' p",
  nextWeek: "eeee 'at' p",
  other: "P",
};

const formatRelative = (token, _date, _baseDate, _options) =>
  formatRelativeLocale[token];

/**
 * The localize function argument callback which allows to convert raw value to
 * the actual type.
 *
 * @param value - The value to convert
 *
 * @returns The converted value
 */

/**
 * The map of localized values for each width.
 */

/**
 * The index type of the locale unit value. It types conversion of units of
 * values that don't start at 0 (i.e. quarters).
 */

/**
 * Converts the unit value to the tuple of values.
 */

/**
 * The tuple of localized era values. The first element represents BC,
 * the second element represents AD.
 */

/**
 * The tuple of localized quarter values. The first element represents Q1.
 */

/**
 * The tuple of localized day values. The first element represents Sunday.
 */

/**
 * The tuple of localized month values. The first element represents January.
 */

function buildLocalizeFn(args) {
  return (value, options) => {
    const context = options?.context ? String(options.context) : "standalone";

    let valuesArray;
    if (context === "formatting" && args.formattingValues) {
      const defaultWidth = args.defaultFormattingWidth || args.defaultWidth;
      const width = options?.width ? String(options.width) : defaultWidth;

      valuesArray =
        args.formattingValues[width] || args.formattingValues[defaultWidth];
    } else {
      const defaultWidth = args.defaultWidth;
      const width = options?.width ? String(options.width) : args.defaultWidth;

      valuesArray = args.values[width] || args.values[defaultWidth];
    }
    const index = args.argumentCallback ? args.argumentCallback(value) : value;

    // @ts-expect-error - For some reason TypeScript just don't want to match it, no matter how hard we try. I challenge you to try to remove it!
    return valuesArray[index];
  };
}

const eraValues = {
  narrow: ["B", "A"],
  abbreviated: ["BC", "AD"],
  wide: ["Before Christ", "Anno Domini"],
};

const quarterValues = {
  narrow: ["1", "2", "3", "4"],
  abbreviated: ["Q1", "Q2", "Q3", "Q4"],
  wide: ["1st quarter", "2nd quarter", "3rd quarter", "4th quarter"],
};

// Note: in English, the names of days of the week and months are capitalized.
// If you are making a new locale based on this one, check if the same is true for the language you're working on.
// Generally, formatted dates should look like they are in the middle of a sentence,
// e.g. in Spanish language the weekdays and months should be in the lowercase.
const monthValues = {
  narrow: ["J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"],
  abbreviated: [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ],

  wide: [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ],
};

const dayValues = {
  narrow: ["S", "M", "T", "W", "T", "F", "S"],
  short: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
  abbreviated: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
  wide: [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
  ],
};

const dayPeriodValues = {
  narrow: {
    am: "a",
    pm: "p",
    midnight: "mi",
    noon: "n",
    morning: "morning",
    afternoon: "afternoon",
    evening: "evening",
    night: "night",
  },
  abbreviated: {
    am: "AM",
    pm: "PM",
    midnight: "midnight",
    noon: "noon",
    morning: "morning",
    afternoon: "afternoon",
    evening: "evening",
    night: "night",
  },
  wide: {
    am: "a.m.",
    pm: "p.m.",
    midnight: "midnight",
    noon: "noon",
    morning: "morning",
    afternoon: "afternoon",
    evening: "evening",
    night: "night",
  },
};

const formattingDayPeriodValues = {
  narrow: {
    am: "a",
    pm: "p",
    midnight: "mi",
    noon: "n",
    morning: "in the morning",
    afternoon: "in the afternoon",
    evening: "in the evening",
    night: "at night",
  },
  abbreviated: {
    am: "AM",
    pm: "PM",
    midnight: "midnight",
    noon: "noon",
    morning: "in the morning",
    afternoon: "in the afternoon",
    evening: "in the evening",
    night: "at night",
  },
  wide: {
    am: "a.m.",
    pm: "p.m.",
    midnight: "midnight",
    noon: "noon",
    morning: "in the morning",
    afternoon: "in the afternoon",
    evening: "in the evening",
    night: "at night",
  },
};

const ordinalNumber = (dirtyNumber, _options) => {
  const number = Number(dirtyNumber);

  // If ordinal numbers depend on context, for example,
  // if they are different for different grammatical genders,
  // use `options.unit`.
  //
  // `unit` can be 'year', 'quarter', 'month', 'week', 'date', 'dayOfYear',
  // 'day', 'hour', 'minute', 'second'.

  const rem100 = number % 100;
  if (rem100 > 20 || rem100 < 10) {
    switch (rem100 % 10) {
      case 1:
        return number + "st";
      case 2:
        return number + "nd";
      case 3:
        return number + "rd";
    }
  }
  return number + "th";
};

const localize = {
  ordinalNumber,

  era: buildLocalizeFn({
    values: eraValues,
    defaultWidth: "wide",
  }),

  quarter: buildLocalizeFn({
    values: quarterValues,
    defaultWidth: "wide",
    argumentCallback: (quarter) => quarter - 1,
  }),

  month: buildLocalizeFn({
    values: monthValues,
    defaultWidth: "wide",
  }),

  day: buildLocalizeFn({
    values: dayValues,
    defaultWidth: "wide",
  }),

  dayPeriod: buildLocalizeFn({
    values: dayPeriodValues,
    defaultWidth: "wide",
    formattingValues: formattingDayPeriodValues,
    defaultFormattingWidth: "wide",
  }),
};

function buildMatchFn(args) {
  return (string, options = {}) => {
    const width = options.width;

    const matchPattern =
      (width && args.matchPatterns[width]) ||
      args.matchPatterns[args.defaultMatchWidth];
    const matchResult = string.match(matchPattern);

    if (!matchResult) {
      return null;
    }
    const matchedString = matchResult[0];

    const parsePatterns =
      (width && args.parsePatterns[width]) ||
      args.parsePatterns[args.defaultParseWidth];

    const key = Array.isArray(parsePatterns)
      ? findIndex(parsePatterns, (pattern) => pattern.test(matchedString))
      : // [TODO] -- I challenge you to fix the type
        findKey(parsePatterns, (pattern) => pattern.test(matchedString));

    let value;

    value = args.valueCallback ? args.valueCallback(key) : key;
    value = options.valueCallback
      ? // [TODO] -- I challenge you to fix the type
        options.valueCallback(value)
      : value;

    const rest = string.slice(matchedString.length);

    return { value, rest };
  };
}

function findKey(object, predicate) {
  for (const key in object) {
    if (
      Object.prototype.hasOwnProperty.call(object, key) &&
      predicate(object[key])
    ) {
      return key;
    }
  }
  return undefined;
}

function findIndex(array, predicate) {
  for (let key = 0; key < array.length; key++) {
    if (predicate(array[key])) {
      return key;
    }
  }
  return undefined;
}

function buildMatchPatternFn(args) {
  return (string, options = {}) => {
    const matchResult = string.match(args.matchPattern);
    if (!matchResult) return null;
    const matchedString = matchResult[0];

    const parseResult = string.match(args.parsePattern);
    if (!parseResult) return null;
    let value = args.valueCallback
      ? args.valueCallback(parseResult[0])
      : parseResult[0];

    // [TODO] I challenge you to fix the type
    value = options.valueCallback ? options.valueCallback(value) : value;

    const rest = string.slice(matchedString.length);

    return { value, rest };
  };
}

const matchOrdinalNumberPattern = /^(\d+)(th|st|nd|rd)?/i;
const parseOrdinalNumberPattern = /\d+/i;

const matchEraPatterns = {
  narrow: /^(b|a)/i,
  abbreviated: /^(b\.?\s?c\.?|b\.?\s?c\.?\s?e\.?|a\.?\s?d\.?|c\.?\s?e\.?)/i,
  wide: /^(before christ|before common era|anno domini|common era)/i,
};
const parseEraPatterns = {
  any: [/^b/i, /^(a|c)/i],
};

const matchQuarterPatterns = {
  narrow: /^[1234]/i,
  abbreviated: /^q[1234]/i,
  wide: /^[1234](th|st|nd|rd)? quarter/i,
};
const parseQuarterPatterns = {
  any: [/1/i, /2/i, /3/i, /4/i],
};

const matchMonthPatterns = {
  narrow: /^[jfmasond]/i,
  abbreviated: /^(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)/i,
  wide: /^(january|february|march|april|may|june|july|august|september|october|november|december)/i,
};
const parseMonthPatterns = {
  narrow: [
    /^j/i,
    /^f/i,
    /^m/i,
    /^a/i,
    /^m/i,
    /^j/i,
    /^j/i,
    /^a/i,
    /^s/i,
    /^o/i,
    /^n/i,
    /^d/i,
  ],

  any: [
    /^ja/i,
    /^f/i,
    /^mar/i,
    /^ap/i,
    /^may/i,
    /^jun/i,
    /^jul/i,
    /^au/i,
    /^s/i,
    /^o/i,
    /^n/i,
    /^d/i,
  ],
};

const matchDayPatterns = {
  narrow: /^[smtwf]/i,
  short: /^(su|mo|tu|we|th|fr|sa)/i,
  abbreviated: /^(sun|mon|tue|wed|thu|fri|sat)/i,
  wide: /^(sunday|monday|tuesday|wednesday|thursday|friday|saturday)/i,
};
const parseDayPatterns = {
  narrow: [/^s/i, /^m/i, /^t/i, /^w/i, /^t/i, /^f/i, /^s/i],
  any: [/^su/i, /^m/i, /^tu/i, /^w/i, /^th/i, /^f/i, /^sa/i],
};

const matchDayPeriodPatterns = {
  narrow: /^(a|p|mi|n|(in the|at) (morning|afternoon|evening|night))/i,
  any: /^([ap]\.?\s?m\.?|midnight|noon|(in the|at) (morning|afternoon|evening|night))/i,
};
const parseDayPeriodPatterns = {
  any: {
    am: /^a/i,
    pm: /^p/i,
    midnight: /^mi/i,
    noon: /^no/i,
    morning: /morning/i,
    afternoon: /afternoon/i,
    evening: /evening/i,
    night: /night/i,
  },
};

const match = {
  ordinalNumber: buildMatchPatternFn({
    matchPattern: matchOrdinalNumberPattern,
    parsePattern: parseOrdinalNumberPattern,
    valueCallback: (value) => parseInt(value, 10),
  }),

  era: buildMatchFn({
    matchPatterns: matchEraPatterns,
    defaultMatchWidth: "wide",
    parsePatterns: parseEraPatterns,
    defaultParseWidth: "any",
  }),

  quarter: buildMatchFn({
    matchPatterns: matchQuarterPatterns,
    defaultMatchWidth: "wide",
    parsePatterns: parseQuarterPatterns,
    defaultParseWidth: "any",
    valueCallback: (index) => index + 1,
  }),

  month: buildMatchFn({
    matchPatterns: matchMonthPatterns,
    defaultMatchWidth: "wide",
    parsePatterns: parseMonthPatterns,
    defaultParseWidth: "any",
  }),

  day: buildMatchFn({
    matchPatterns: matchDayPatterns,
    defaultMatchWidth: "wide",
    parsePatterns: parseDayPatterns,
    defaultParseWidth: "any",
  }),

  dayPeriod: buildMatchFn({
    matchPatterns: matchDayPeriodPatterns,
    defaultMatchWidth: "any",
    parsePatterns: parseDayPeriodPatterns,
    defaultParseWidth: "any",
  }),
};

/**
 * @category Locales
 * @summary English locale (United States).
 * @language English
 * @iso-639-2 eng
 * @author Sasha Koss [@kossnocorp](https://github.com/kossnocorp)
 * @author Lesha Koss [@leshakoss](https://github.com/leshakoss)
 */
const enUS$1 = {
  code: "en-US",
  formatDistance: formatDistance,
  formatLong: formatLong,
  formatRelative: formatRelative,
  localize: localize,
  match: match,
  options: {
    weekStartsOn: 0 /* Sunday */,
    firstWeekContainsDate: 1,
  },
};

/**
 * The {@link getDayOfYear} function options.
 */

/**
 * @name getDayOfYear
 * @category Day Helpers
 * @summary Get the day of the year of the given date.
 *
 * @description
 * Get the day of the year of the given date.
 *
 * @param date - The given date
 * @param options - The options
 *
 * @returns The day of year
 *
 * @example
 * // Which day of the year is 2 July 2014?
 * const result = getDayOfYear(new Date(2014, 6, 2))
 * //=> 183
 */
function getDayOfYear(date, options) {
  const _date = toDate(date, options?.in);
  const diff = differenceInCalendarDays(_date, startOfYear(_date));
  const dayOfYear = diff + 1;
  return dayOfYear;
}

/**
 * The {@link getISOWeek} function options.
 */

/**
 * @name getISOWeek
 * @category ISO Week Helpers
 * @summary Get the ISO week of the given date.
 *
 * @description
 * Get the ISO week of the given date.
 *
 * ISO week-numbering year: http://en.wikipedia.org/wiki/ISO_week_date
 *
 * @param date - The given date
 * @param options - The options
 *
 * @returns The ISO week
 *
 * @example
 * // Which week of the ISO-week numbering year is 2 January 2005?
 * const result = getISOWeek(new Date(2005, 0, 2))
 * //=> 53
 */
function getISOWeek(date, options) {
  const _date = toDate(date, options?.in);
  const diff = +startOfISOWeek(_date) - +startOfISOWeekYear(_date);

  // Round the number of weeks to the nearest integer because the number of
  // milliseconds in a week is not constant (e.g. it's different in the week of
  // the daylight saving time clock shift).
  return Math.round(diff / millisecondsInWeek) + 1;
}

/**
 * The {@link getWeekYear} function options.
 */

/**
 * @name getWeekYear
 * @category Week-Numbering Year Helpers
 * @summary Get the local week-numbering year of the given date.
 *
 * @description
 * Get the local week-numbering year of the given date.
 * The exact calculation depends on the values of
 * `options.weekStartsOn` (which is the index of the first day of the week)
 * and `options.firstWeekContainsDate` (which is the day of January, which is always in
 * the first week of the week-numbering year)
 *
 * Week numbering: https://en.wikipedia.org/wiki/Week#The_ISO_week_date_system
 *
 * @param date - The given date
 * @param options - An object with options.
 *
 * @returns The local week-numbering year
 *
 * @example
 * // Which week numbering year is 26 December 2004 with the default settings?
 * const result = getWeekYear(new Date(2004, 11, 26))
 * //=> 2005
 *
 * @example
 * // Which week numbering year is 26 December 2004 if week starts on Saturday?
 * const result = getWeekYear(new Date(2004, 11, 26), { weekStartsOn: 6 })
 * //=> 2004
 *
 * @example
 * // Which week numbering year is 26 December 2004 if the first week contains 4 January?
 * const result = getWeekYear(new Date(2004, 11, 26), { firstWeekContainsDate: 4 })
 * //=> 2004
 */
function getWeekYear(date, options) {
  const _date = toDate(date, options?.in);
  const year = _date.getFullYear();

  const defaultOptions = getDefaultOptions();
  const firstWeekContainsDate =
    options?.firstWeekContainsDate ??
    options?.locale?.options?.firstWeekContainsDate ??
    defaultOptions.firstWeekContainsDate ??
    defaultOptions.locale?.options?.firstWeekContainsDate ??
    1;

  const firstWeekOfNextYear = constructFrom(options?.in || date, 0);
  firstWeekOfNextYear.setFullYear(year + 1, 0, firstWeekContainsDate);
  firstWeekOfNextYear.setHours(0, 0, 0, 0);
  const startOfNextYear = startOfWeek(firstWeekOfNextYear, options);

  const firstWeekOfThisYear = constructFrom(options?.in || date, 0);
  firstWeekOfThisYear.setFullYear(year, 0, firstWeekContainsDate);
  firstWeekOfThisYear.setHours(0, 0, 0, 0);
  const startOfThisYear = startOfWeek(firstWeekOfThisYear, options);

  if (+_date >= +startOfNextYear) {
    return year + 1;
  } else if (+_date >= +startOfThisYear) {
    return year;
  } else {
    return year - 1;
  }
}

/**
 * The {@link startOfWeekYear} function options.
 */

/**
 * @name startOfWeekYear
 * @category Week-Numbering Year Helpers
 * @summary Return the start of a local week-numbering year for the given date.
 *
 * @description
 * Return the start of a local week-numbering year.
 * The exact calculation depends on the values of
 * `options.weekStartsOn` (which is the index of the first day of the week)
 * and `options.firstWeekContainsDate` (which is the day of January, which is always in
 * the first week of the week-numbering year)
 *
 * Week numbering: https://en.wikipedia.org/wiki/Week#The_ISO_week_date_system
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type.
 *
 * @param date - The original date
 * @param options - An object with options
 *
 * @returns The start of a week-numbering year
 *
 * @example
 * // The start of an a week-numbering year for 2 July 2005 with default settings:
 * const result = startOfWeekYear(new Date(2005, 6, 2))
 * //=> Sun Dec 26 2004 00:00:00
 *
 * @example
 * // The start of a week-numbering year for 2 July 2005
 * // if Monday is the first day of week
 * // and 4 January is always in the first week of the year:
 * const result = startOfWeekYear(new Date(2005, 6, 2), {
 *   weekStartsOn: 1,
 *   firstWeekContainsDate: 4
 * })
 * //=> Mon Jan 03 2005 00:00:00
 */
function startOfWeekYear(date, options) {
  const defaultOptions = getDefaultOptions();
  const firstWeekContainsDate =
    options?.firstWeekContainsDate ??
    options?.locale?.options?.firstWeekContainsDate ??
    defaultOptions.firstWeekContainsDate ??
    defaultOptions.locale?.options?.firstWeekContainsDate ??
    1;

  const year = getWeekYear(date, options);
  const firstWeek = constructFrom(options?.in || date, 0);
  firstWeek.setFullYear(year, 0, firstWeekContainsDate);
  firstWeek.setHours(0, 0, 0, 0);
  const _date = startOfWeek(firstWeek, options);
  return _date;
}

/**
 * The {@link getWeek} function options.
 */

/**
 * @name getWeek
 * @category Week Helpers
 * @summary Get the local week index of the given date.
 *
 * @description
 * Get the local week index of the given date.
 * The exact calculation depends on the values of
 * `options.weekStartsOn` (which is the index of the first day of the week)
 * and `options.firstWeekContainsDate` (which is the day of January, which is always in
 * the first week of the week-numbering year)
 *
 * Week numbering: https://en.wikipedia.org/wiki/Week#The_ISO_week_date_system
 *
 * @param date - The given date
 * @param options - An object with options
 *
 * @returns The week
 *
 * @example
 * // Which week of the local week numbering year is 2 January 2005 with default options?
 * const result = getWeek(new Date(2005, 0, 2))
 * //=> 2
 *
 * @example
 * // Which week of the local week numbering year is 2 January 2005,
 * // if Monday is the first day of the week,
 * // and the first week of the year always contains 4 January?
 * const result = getWeek(new Date(2005, 0, 2), {
 *   weekStartsOn: 1,
 *   firstWeekContainsDate: 4
 * })
 * //=> 53
 */
function getWeek(date, options) {
  const _date = toDate(date, options?.in);
  const diff = +startOfWeek(_date, options) - +startOfWeekYear(_date, options);

  // Round the number of weeks to the nearest integer because the number of
  // milliseconds in a week is not constant (e.g. it's different in the week of
  // the daylight saving time clock shift).
  return Math.round(diff / millisecondsInWeek) + 1;
}

function addLeadingZeros(number, targetLength) {
  const sign = number < 0 ? "-" : "";
  const output = Math.abs(number).toString().padStart(targetLength, "0");
  return sign + output;
}

/*
 * |     | Unit                           |     | Unit                           |
 * |-----|--------------------------------|-----|--------------------------------|
 * |  a  | AM, PM                         |  A* |                                |
 * |  d  | Day of month                   |  D  |                                |
 * |  h  | Hour [1-12]                    |  H  | Hour [0-23]                    |
 * |  m  | Minute                         |  M  | Month                          |
 * |  s  | Second                         |  S  | Fraction of second             |
 * |  y  | Year (abs)                     |  Y  |                                |
 *
 * Letters marked by * are not implemented but reserved by Unicode standard.
 */

const lightFormatters = {
  // Year
  y(date, token) {
    // From http://www.unicode.org/reports/tr35/tr35-31/tr35-dates.html#Date_Format_tokens
    // | Year     |     y | yy |   yyy |  yyyy | yyyyy |
    // |----------|-------|----|-------|-------|-------|
    // | AD 1     |     1 | 01 |   001 |  0001 | 00001 |
    // | AD 12    |    12 | 12 |   012 |  0012 | 00012 |
    // | AD 123   |   123 | 23 |   123 |  0123 | 00123 |
    // | AD 1234  |  1234 | 34 |  1234 |  1234 | 01234 |
    // | AD 12345 | 12345 | 45 | 12345 | 12345 | 12345 |

    const signedYear = date.getFullYear();
    // Returns 1 for 1 BC (which is year 0 in JavaScript)
    const year = signedYear > 0 ? signedYear : 1 - signedYear;
    return addLeadingZeros(token === "yy" ? year % 100 : year, token.length);
  },

  // Month
  M(date, token) {
    const month = date.getMonth();
    return token === "M" ? String(month + 1) : addLeadingZeros(month + 1, 2);
  },

  // Day of the month
  d(date, token) {
    return addLeadingZeros(date.getDate(), token.length);
  },

  // AM or PM
  a(date, token) {
    const dayPeriodEnumValue = date.getHours() / 12 >= 1 ? "pm" : "am";

    switch (token) {
      case "a":
      case "aa":
        return dayPeriodEnumValue.toUpperCase();
      case "aaa":
        return dayPeriodEnumValue;
      case "aaaaa":
        return dayPeriodEnumValue[0];
      case "aaaa":
      default:
        return dayPeriodEnumValue === "am" ? "a.m." : "p.m.";
    }
  },

  // Hour [1-12]
  h(date, token) {
    return addLeadingZeros(date.getHours() % 12 || 12, token.length);
  },

  // Hour [0-23]
  H(date, token) {
    return addLeadingZeros(date.getHours(), token.length);
  },

  // Minute
  m(date, token) {
    return addLeadingZeros(date.getMinutes(), token.length);
  },

  // Second
  s(date, token) {
    return addLeadingZeros(date.getSeconds(), token.length);
  },

  // Fraction of second
  S(date, token) {
    const numberOfDigits = token.length;
    const milliseconds = date.getMilliseconds();
    const fractionalSeconds = Math.trunc(
      milliseconds * Math.pow(10, numberOfDigits - 3),
    );
    return addLeadingZeros(fractionalSeconds, token.length);
  },
};

const dayPeriodEnum = {
  midnight: "midnight",
  noon: "noon",
  morning: "morning",
  afternoon: "afternoon",
  evening: "evening",
  night: "night",
};

/*
 * |     | Unit                           |     | Unit                           |
 * |-----|--------------------------------|-----|--------------------------------|
 * |  a  | AM, PM                         |  A* | Milliseconds in day            |
 * |  b  | AM, PM, noon, midnight         |  B  | Flexible day period            |
 * |  c  | Stand-alone local day of week  |  C* | Localized hour w/ day period   |
 * |  d  | Day of month                   |  D  | Day of year                    |
 * |  e  | Local day of week              |  E  | Day of week                    |
 * |  f  |                                |  F* | Day of week in month           |
 * |  g* | Modified Julian day            |  G  | Era                            |
 * |  h  | Hour [1-12]                    |  H  | Hour [0-23]                    |
 * |  i! | ISO day of week                |  I! | ISO week of year               |
 * |  j* | Localized hour w/ day period   |  J* | Localized hour w/o day period  |
 * |  k  | Hour [1-24]                    |  K  | Hour [0-11]                    |
 * |  l* | (deprecated)                   |  L  | Stand-alone month              |
 * |  m  | Minute                         |  M  | Month                          |
 * |  n  |                                |  N  |                                |
 * |  o! | Ordinal number modifier        |  O  | Timezone (GMT)                 |
 * |  p! | Long localized time            |  P! | Long localized date            |
 * |  q  | Stand-alone quarter            |  Q  | Quarter                        |
 * |  r* | Related Gregorian year         |  R! | ISO week-numbering year        |
 * |  s  | Second                         |  S  | Fraction of second             |
 * |  t! | Seconds timestamp              |  T! | Milliseconds timestamp         |
 * |  u  | Extended year                  |  U* | Cyclic year                    |
 * |  v* | Timezone (generic non-locat.)  |  V* | Timezone (location)            |
 * |  w  | Local week of year             |  W* | Week of month                  |
 * |  x  | Timezone (ISO-8601 w/o Z)      |  X  | Timezone (ISO-8601)            |
 * |  y  | Year (abs)                     |  Y  | Local week-numbering year      |
 * |  z  | Timezone (specific non-locat.) |  Z* | Timezone (aliases)             |
 *
 * Letters marked by * are not implemented but reserved by Unicode standard.
 *
 * Letters marked by ! are non-standard, but implemented by date-fns:
 * - `o` modifies the previous token to turn it into an ordinal (see `format` docs)
 * - `i` is ISO day of week. For `i` and `ii` is returns numeric ISO week days,
 *   i.e. 7 for Sunday, 1 for Monday, etc.
 * - `I` is ISO week of year, as opposed to `w` which is local week of year.
 * - `R` is ISO week-numbering year, as opposed to `Y` which is local week-numbering year.
 *   `R` is supposed to be used in conjunction with `I` and `i`
 *   for universal ISO week-numbering date, whereas
 *   `Y` is supposed to be used in conjunction with `w` and `e`
 *   for week-numbering date specific to the locale.
 * - `P` is long localized date format
 * - `p` is long localized time format
 */

const formatters = {
  // Era
  G: function (date, token, localize) {
    const era = date.getFullYear() > 0 ? 1 : 0;
    switch (token) {
      // AD, BC
      case "G":
      case "GG":
      case "GGG":
        return localize.era(era, { width: "abbreviated" });
      // A, B
      case "GGGGG":
        return localize.era(era, { width: "narrow" });
      // Anno Domini, Before Christ
      case "GGGG":
      default:
        return localize.era(era, { width: "wide" });
    }
  },

  // Year
  y: function (date, token, localize) {
    // Ordinal number
    if (token === "yo") {
      const signedYear = date.getFullYear();
      // Returns 1 for 1 BC (which is year 0 in JavaScript)
      const year = signedYear > 0 ? signedYear : 1 - signedYear;
      return localize.ordinalNumber(year, { unit: "year" });
    }

    return lightFormatters.y(date, token);
  },

  // Local week-numbering year
  Y: function (date, token, localize, options) {
    const signedWeekYear = getWeekYear(date, options);
    // Returns 1 for 1 BC (which is year 0 in JavaScript)
    const weekYear = signedWeekYear > 0 ? signedWeekYear : 1 - signedWeekYear;

    // Two digit year
    if (token === "YY") {
      const twoDigitYear = weekYear % 100;
      return addLeadingZeros(twoDigitYear, 2);
    }

    // Ordinal number
    if (token === "Yo") {
      return localize.ordinalNumber(weekYear, { unit: "year" });
    }

    // Padding
    return addLeadingZeros(weekYear, token.length);
  },

  // ISO week-numbering year
  R: function (date, token) {
    const isoWeekYear = getISOWeekYear(date);

    // Padding
    return addLeadingZeros(isoWeekYear, token.length);
  },

  // Extended year. This is a single number designating the year of this calendar system.
  // The main difference between `y` and `u` localizers are B.C. years:
  // | Year | `y` | `u` |
  // |------|-----|-----|
  // | AC 1 |   1 |   1 |
  // | BC 1 |   1 |   0 |
  // | BC 2 |   2 |  -1 |
  // Also `yy` always returns the last two digits of a year,
  // while `uu` pads single digit years to 2 characters and returns other years unchanged.
  u: function (date, token) {
    const year = date.getFullYear();
    return addLeadingZeros(year, token.length);
  },

  // Quarter
  Q: function (date, token, localize) {
    const quarter = Math.ceil((date.getMonth() + 1) / 3);
    switch (token) {
      // 1, 2, 3, 4
      case "Q":
        return String(quarter);
      // 01, 02, 03, 04
      case "QQ":
        return addLeadingZeros(quarter, 2);
      // 1st, 2nd, 3rd, 4th
      case "Qo":
        return localize.ordinalNumber(quarter, { unit: "quarter" });
      // Q1, Q2, Q3, Q4
      case "QQQ":
        return localize.quarter(quarter, {
          width: "abbreviated",
          context: "formatting",
        });
      // 1, 2, 3, 4 (narrow quarter; could be not numerical)
      case "QQQQQ":
        return localize.quarter(quarter, {
          width: "narrow",
          context: "formatting",
        });
      // 1st quarter, 2nd quarter, ...
      case "QQQQ":
      default:
        return localize.quarter(quarter, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // Stand-alone quarter
  q: function (date, token, localize) {
    const quarter = Math.ceil((date.getMonth() + 1) / 3);
    switch (token) {
      // 1, 2, 3, 4
      case "q":
        return String(quarter);
      // 01, 02, 03, 04
      case "qq":
        return addLeadingZeros(quarter, 2);
      // 1st, 2nd, 3rd, 4th
      case "qo":
        return localize.ordinalNumber(quarter, { unit: "quarter" });
      // Q1, Q2, Q3, Q4
      case "qqq":
        return localize.quarter(quarter, {
          width: "abbreviated",
          context: "standalone",
        });
      // 1, 2, 3, 4 (narrow quarter; could be not numerical)
      case "qqqqq":
        return localize.quarter(quarter, {
          width: "narrow",
          context: "standalone",
        });
      // 1st quarter, 2nd quarter, ...
      case "qqqq":
      default:
        return localize.quarter(quarter, {
          width: "wide",
          context: "standalone",
        });
    }
  },

  // Month
  M: function (date, token, localize) {
    const month = date.getMonth();
    switch (token) {
      case "M":
      case "MM":
        return lightFormatters.M(date, token);
      // 1st, 2nd, ..., 12th
      case "Mo":
        return localize.ordinalNumber(month + 1, { unit: "month" });
      // Jan, Feb, ..., Dec
      case "MMM":
        return localize.month(month, {
          width: "abbreviated",
          context: "formatting",
        });
      // J, F, ..., D
      case "MMMMM":
        return localize.month(month, {
          width: "narrow",
          context: "formatting",
        });
      // January, February, ..., December
      case "MMMM":
      default:
        return localize.month(month, { width: "wide", context: "formatting" });
    }
  },

  // Stand-alone month
  L: function (date, token, localize) {
    const month = date.getMonth();
    switch (token) {
      // 1, 2, ..., 12
      case "L":
        return String(month + 1);
      // 01, 02, ..., 12
      case "LL":
        return addLeadingZeros(month + 1, 2);
      // 1st, 2nd, ..., 12th
      case "Lo":
        return localize.ordinalNumber(month + 1, { unit: "month" });
      // Jan, Feb, ..., Dec
      case "LLL":
        return localize.month(month, {
          width: "abbreviated",
          context: "standalone",
        });
      // J, F, ..., D
      case "LLLLL":
        return localize.month(month, {
          width: "narrow",
          context: "standalone",
        });
      // January, February, ..., December
      case "LLLL":
      default:
        return localize.month(month, { width: "wide", context: "standalone" });
    }
  },

  // Local week of year
  w: function (date, token, localize, options) {
    const week = getWeek(date, options);

    if (token === "wo") {
      return localize.ordinalNumber(week, { unit: "week" });
    }

    return addLeadingZeros(week, token.length);
  },

  // ISO week of year
  I: function (date, token, localize) {
    const isoWeek = getISOWeek(date);

    if (token === "Io") {
      return localize.ordinalNumber(isoWeek, { unit: "week" });
    }

    return addLeadingZeros(isoWeek, token.length);
  },

  // Day of the month
  d: function (date, token, localize) {
    if (token === "do") {
      return localize.ordinalNumber(date.getDate(), { unit: "date" });
    }

    return lightFormatters.d(date, token);
  },

  // Day of year
  D: function (date, token, localize) {
    const dayOfYear = getDayOfYear(date);

    if (token === "Do") {
      return localize.ordinalNumber(dayOfYear, { unit: "dayOfYear" });
    }

    return addLeadingZeros(dayOfYear, token.length);
  },

  // Day of week
  E: function (date, token, localize) {
    const dayOfWeek = date.getDay();
    switch (token) {
      // Tue
      case "E":
      case "EE":
      case "EEE":
        return localize.day(dayOfWeek, {
          width: "abbreviated",
          context: "formatting",
        });
      // T
      case "EEEEE":
        return localize.day(dayOfWeek, {
          width: "narrow",
          context: "formatting",
        });
      // Tu
      case "EEEEEE":
        return localize.day(dayOfWeek, {
          width: "short",
          context: "formatting",
        });
      // Tuesday
      case "EEEE":
      default:
        return localize.day(dayOfWeek, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // Local day of week
  e: function (date, token, localize, options) {
    const dayOfWeek = date.getDay();
    const localDayOfWeek = (dayOfWeek - options.weekStartsOn + 8) % 7 || 7;
    switch (token) {
      // Numerical value (Nth day of week with current locale or weekStartsOn)
      case "e":
        return String(localDayOfWeek);
      // Padded numerical value
      case "ee":
        return addLeadingZeros(localDayOfWeek, 2);
      // 1st, 2nd, ..., 7th
      case "eo":
        return localize.ordinalNumber(localDayOfWeek, { unit: "day" });
      case "eee":
        return localize.day(dayOfWeek, {
          width: "abbreviated",
          context: "formatting",
        });
      // T
      case "eeeee":
        return localize.day(dayOfWeek, {
          width: "narrow",
          context: "formatting",
        });
      // Tu
      case "eeeeee":
        return localize.day(dayOfWeek, {
          width: "short",
          context: "formatting",
        });
      // Tuesday
      case "eeee":
      default:
        return localize.day(dayOfWeek, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // Stand-alone local day of week
  c: function (date, token, localize, options) {
    const dayOfWeek = date.getDay();
    const localDayOfWeek = (dayOfWeek - options.weekStartsOn + 8) % 7 || 7;
    switch (token) {
      // Numerical value (same as in `e`)
      case "c":
        return String(localDayOfWeek);
      // Padded numerical value
      case "cc":
        return addLeadingZeros(localDayOfWeek, token.length);
      // 1st, 2nd, ..., 7th
      case "co":
        return localize.ordinalNumber(localDayOfWeek, { unit: "day" });
      case "ccc":
        return localize.day(dayOfWeek, {
          width: "abbreviated",
          context: "standalone",
        });
      // T
      case "ccccc":
        return localize.day(dayOfWeek, {
          width: "narrow",
          context: "standalone",
        });
      // Tu
      case "cccccc":
        return localize.day(dayOfWeek, {
          width: "short",
          context: "standalone",
        });
      // Tuesday
      case "cccc":
      default:
        return localize.day(dayOfWeek, {
          width: "wide",
          context: "standalone",
        });
    }
  },

  // ISO day of week
  i: function (date, token, localize) {
    const dayOfWeek = date.getDay();
    const isoDayOfWeek = dayOfWeek === 0 ? 7 : dayOfWeek;
    switch (token) {
      // 2
      case "i":
        return String(isoDayOfWeek);
      // 02
      case "ii":
        return addLeadingZeros(isoDayOfWeek, token.length);
      // 2nd
      case "io":
        return localize.ordinalNumber(isoDayOfWeek, { unit: "day" });
      // Tue
      case "iii":
        return localize.day(dayOfWeek, {
          width: "abbreviated",
          context: "formatting",
        });
      // T
      case "iiiii":
        return localize.day(dayOfWeek, {
          width: "narrow",
          context: "formatting",
        });
      // Tu
      case "iiiiii":
        return localize.day(dayOfWeek, {
          width: "short",
          context: "formatting",
        });
      // Tuesday
      case "iiii":
      default:
        return localize.day(dayOfWeek, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // AM or PM
  a: function (date, token, localize) {
    const hours = date.getHours();
    const dayPeriodEnumValue = hours / 12 >= 1 ? "pm" : "am";

    switch (token) {
      case "a":
      case "aa":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "abbreviated",
          context: "formatting",
        });
      case "aaa":
        return localize
          .dayPeriod(dayPeriodEnumValue, {
            width: "abbreviated",
            context: "formatting",
          })
          .toLowerCase();
      case "aaaaa":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "narrow",
          context: "formatting",
        });
      case "aaaa":
      default:
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // AM, PM, midnight, noon
  b: function (date, token, localize) {
    const hours = date.getHours();
    let dayPeriodEnumValue;
    if (hours === 12) {
      dayPeriodEnumValue = dayPeriodEnum.noon;
    } else if (hours === 0) {
      dayPeriodEnumValue = dayPeriodEnum.midnight;
    } else {
      dayPeriodEnumValue = hours / 12 >= 1 ? "pm" : "am";
    }

    switch (token) {
      case "b":
      case "bb":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "abbreviated",
          context: "formatting",
        });
      case "bbb":
        return localize
          .dayPeriod(dayPeriodEnumValue, {
            width: "abbreviated",
            context: "formatting",
          })
          .toLowerCase();
      case "bbbbb":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "narrow",
          context: "formatting",
        });
      case "bbbb":
      default:
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // in the morning, in the afternoon, in the evening, at night
  B: function (date, token, localize) {
    const hours = date.getHours();
    let dayPeriodEnumValue;
    if (hours >= 17) {
      dayPeriodEnumValue = dayPeriodEnum.evening;
    } else if (hours >= 12) {
      dayPeriodEnumValue = dayPeriodEnum.afternoon;
    } else if (hours >= 4) {
      dayPeriodEnumValue = dayPeriodEnum.morning;
    } else {
      dayPeriodEnumValue = dayPeriodEnum.night;
    }

    switch (token) {
      case "B":
      case "BB":
      case "BBB":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "abbreviated",
          context: "formatting",
        });
      case "BBBBB":
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "narrow",
          context: "formatting",
        });
      case "BBBB":
      default:
        return localize.dayPeriod(dayPeriodEnumValue, {
          width: "wide",
          context: "formatting",
        });
    }
  },

  // Hour [1-12]
  h: function (date, token, localize) {
    if (token === "ho") {
      let hours = date.getHours() % 12;
      if (hours === 0) hours = 12;
      return localize.ordinalNumber(hours, { unit: "hour" });
    }

    return lightFormatters.h(date, token);
  },

  // Hour [0-23]
  H: function (date, token, localize) {
    if (token === "Ho") {
      return localize.ordinalNumber(date.getHours(), { unit: "hour" });
    }

    return lightFormatters.H(date, token);
  },

  // Hour [0-11]
  K: function (date, token, localize) {
    const hours = date.getHours() % 12;

    if (token === "Ko") {
      return localize.ordinalNumber(hours, { unit: "hour" });
    }

    return addLeadingZeros(hours, token.length);
  },

  // Hour [1-24]
  k: function (date, token, localize) {
    let hours = date.getHours();
    if (hours === 0) hours = 24;

    if (token === "ko") {
      return localize.ordinalNumber(hours, { unit: "hour" });
    }

    return addLeadingZeros(hours, token.length);
  },

  // Minute
  m: function (date, token, localize) {
    if (token === "mo") {
      return localize.ordinalNumber(date.getMinutes(), { unit: "minute" });
    }

    return lightFormatters.m(date, token);
  },

  // Second
  s: function (date, token, localize) {
    if (token === "so") {
      return localize.ordinalNumber(date.getSeconds(), { unit: "second" });
    }

    return lightFormatters.s(date, token);
  },

  // Fraction of second
  S: function (date, token) {
    return lightFormatters.S(date, token);
  },

  // Timezone (ISO-8601. If offset is 0, output is always `'Z'`)
  X: function (date, token, _localize) {
    const timezoneOffset = date.getTimezoneOffset();

    if (timezoneOffset === 0) {
      return "Z";
    }

    switch (token) {
      // Hours and optional minutes
      case "X":
        return formatTimezoneWithOptionalMinutes(timezoneOffset);

      // Hours, minutes and optional seconds without `:` delimiter
      // Note: neither ISO-8601 nor JavaScript supports seconds in timezone offsets
      // so this token always has the same output as `XX`
      case "XXXX":
      case "XX": // Hours and minutes without `:` delimiter
        return formatTimezone(timezoneOffset);

      // Hours, minutes and optional seconds with `:` delimiter
      // Note: neither ISO-8601 nor JavaScript supports seconds in timezone offsets
      // so this token always has the same output as `XXX`
      case "XXXXX":
      case "XXX": // Hours and minutes with `:` delimiter
      default:
        return formatTimezone(timezoneOffset, ":");
    }
  },

  // Timezone (ISO-8601. If offset is 0, output is `'+00:00'` or equivalent)
  x: function (date, token, _localize) {
    const timezoneOffset = date.getTimezoneOffset();

    switch (token) {
      // Hours and optional minutes
      case "x":
        return formatTimezoneWithOptionalMinutes(timezoneOffset);

      // Hours, minutes and optional seconds without `:` delimiter
      // Note: neither ISO-8601 nor JavaScript supports seconds in timezone offsets
      // so this token always has the same output as `xx`
      case "xxxx":
      case "xx": // Hours and minutes without `:` delimiter
        return formatTimezone(timezoneOffset);

      // Hours, minutes and optional seconds with `:` delimiter
      // Note: neither ISO-8601 nor JavaScript supports seconds in timezone offsets
      // so this token always has the same output as `xxx`
      case "xxxxx":
      case "xxx": // Hours and minutes with `:` delimiter
      default:
        return formatTimezone(timezoneOffset, ":");
    }
  },

  // Timezone (GMT)
  O: function (date, token, _localize) {
    const timezoneOffset = date.getTimezoneOffset();

    switch (token) {
      // Short
      case "O":
      case "OO":
      case "OOO":
        return "GMT" + formatTimezoneShort(timezoneOffset, ":");
      // Long
      case "OOOO":
      default:
        return "GMT" + formatTimezone(timezoneOffset, ":");
    }
  },

  // Timezone (specific non-location)
  z: function (date, token, _localize) {
    const timezoneOffset = date.getTimezoneOffset();

    switch (token) {
      // Short
      case "z":
      case "zz":
      case "zzz":
        return "GMT" + formatTimezoneShort(timezoneOffset, ":");
      // Long
      case "zzzz":
      default:
        return "GMT" + formatTimezone(timezoneOffset, ":");
    }
  },

  // Seconds timestamp
  t: function (date, token, _localize) {
    const timestamp = Math.trunc(+date / 1000);
    return addLeadingZeros(timestamp, token.length);
  },

  // Milliseconds timestamp
  T: function (date, token, _localize) {
    return addLeadingZeros(+date, token.length);
  },
};

function formatTimezoneShort(offset, delimiter = "") {
  const sign = offset > 0 ? "-" : "+";
  const absOffset = Math.abs(offset);
  const hours = Math.trunc(absOffset / 60);
  const minutes = absOffset % 60;
  if (minutes === 0) {
    return sign + String(hours);
  }
  return sign + String(hours) + delimiter + addLeadingZeros(minutes, 2);
}

function formatTimezoneWithOptionalMinutes(offset, delimiter) {
  if (offset % 60 === 0) {
    const sign = offset > 0 ? "-" : "+";
    return sign + addLeadingZeros(Math.abs(offset) / 60, 2);
  }
  return formatTimezone(offset, delimiter);
}

function formatTimezone(offset, delimiter = "") {
  const sign = offset > 0 ? "-" : "+";
  const absOffset = Math.abs(offset);
  const hours = addLeadingZeros(Math.trunc(absOffset / 60), 2);
  const minutes = addLeadingZeros(absOffset % 60, 2);
  return sign + hours + delimiter + minutes;
}

const dateLongFormatter = (pattern, formatLong) => {
  switch (pattern) {
    case "P":
      return formatLong.date({ width: "short" });
    case "PP":
      return formatLong.date({ width: "medium" });
    case "PPP":
      return formatLong.date({ width: "long" });
    case "PPPP":
    default:
      return formatLong.date({ width: "full" });
  }
};

const timeLongFormatter = (pattern, formatLong) => {
  switch (pattern) {
    case "p":
      return formatLong.time({ width: "short" });
    case "pp":
      return formatLong.time({ width: "medium" });
    case "ppp":
      return formatLong.time({ width: "long" });
    case "pppp":
    default:
      return formatLong.time({ width: "full" });
  }
};

const dateTimeLongFormatter = (pattern, formatLong) => {
  const matchResult = pattern.match(/(P+)(p+)?/) || [];
  const datePattern = matchResult[1];
  const timePattern = matchResult[2];

  if (!timePattern) {
    return dateLongFormatter(pattern, formatLong);
  }

  let dateTimeFormat;

  switch (datePattern) {
    case "P":
      dateTimeFormat = formatLong.dateTime({ width: "short" });
      break;
    case "PP":
      dateTimeFormat = formatLong.dateTime({ width: "medium" });
      break;
    case "PPP":
      dateTimeFormat = formatLong.dateTime({ width: "long" });
      break;
    case "PPPP":
    default:
      dateTimeFormat = formatLong.dateTime({ width: "full" });
      break;
  }

  return dateTimeFormat
    .replace("{{date}}", dateLongFormatter(datePattern, formatLong))
    .replace("{{time}}", timeLongFormatter(timePattern, formatLong));
};

const longFormatters = {
  p: timeLongFormatter,
  P: dateTimeLongFormatter,
};

const dayOfYearTokenRE = /^D+$/;
const weekYearTokenRE = /^Y+$/;

const throwTokens = ["D", "DD", "YY", "YYYY"];

function isProtectedDayOfYearToken(token) {
  return dayOfYearTokenRE.test(token);
}

function isProtectedWeekYearToken(token) {
  return weekYearTokenRE.test(token);
}

function warnOrThrowProtectedError(token, format, input) {
  const _message = message(token, format, input);
  console.warn(_message);
  if (throwTokens.includes(token)) throw new RangeError(_message);
}

function message(token, format, input) {
  const subject = token[0] === "Y" ? "years" : "days of the month";
  return `Use \`${token.toLowerCase()}\` instead of \`${token}\` (in \`${format}\`) for formatting ${subject} to the input \`${input}\`; see: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md`;
}

// This RegExp consists of three parts separated by `|`:
// - [yYQqMLwIdDecihHKkms]o matches any available ordinal number token
//   (one of the certain letters followed by `o`)
// - (\w)\1* matches any sequences of the same letter
// - '' matches two quote characters in a row
// - '(''|[^'])+('|$) matches anything surrounded by two quote characters ('),
//   except a single quote symbol, which ends the sequence.
//   Two quote characters do not end the sequence.
//   If there is no matching single quote
//   then the sequence will continue until the end of the string.
// - . matches any single character unmatched by previous parts of the RegExps
const formattingTokensRegExp =
  /[yYQqMLwIdDecihHKkms]o|(\w)\1*|''|'(''|[^'])+('|$)|./g;

// This RegExp catches symbols escaped by quotes, and also
// sequences of symbols P, p, and the combinations like `PPPPPPPppppp`
const longFormattingTokensRegExp = /P+p+|P+|p+|''|'(''|[^'])+('|$)|./g;

const escapedStringRegExp = /^'([^]*?)'?$/;
const doubleQuoteRegExp = /''/g;
const unescapedLatinCharacterRegExp = /[a-zA-Z]/;

/**
 * The {@link format} function options.
 */

/**
 * @name format
 * @alias formatDate
 * @category Common Helpers
 * @summary Format the date.
 *
 * @description
 * Return the formatted date string in the given format. The result may vary by locale.
 *
 * > ⚠️ Please note that the `format` tokens differ from Moment.js and other libraries.
 * > See: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 *
 * The characters wrapped between two single quotes characters (') are escaped.
 * Two single quotes in a row, whether inside or outside a quoted sequence, represent a 'real' single quote.
 * (see the last example)
 *
 * Format of the string is based on Unicode Technical Standard #35:
 * https://www.unicode.org/reports/tr35/tr35-dates.html#Date_Field_Symbol_Table
 * with a few additions (see note 7 below the table).
 *
 * Accepted patterns:
 * | Unit                            | Pattern | Result examples                   | Notes |
 * |---------------------------------|---------|-----------------------------------|-------|
 * | Era                             | G..GGG  | AD, BC                            |       |
 * |                                 | GGGG    | Anno Domini, Before Christ        | 2     |
 * |                                 | GGGGG   | A, B                              |       |
 * | Calendar year                   | y       | 44, 1, 1900, 2017                 | 5     |
 * |                                 | yo      | 44th, 1st, 0th, 17th              | 5,7   |
 * |                                 | yy      | 44, 01, 00, 17                    | 5     |
 * |                                 | yyy     | 044, 001, 1900, 2017              | 5     |
 * |                                 | yyyy    | 0044, 0001, 1900, 2017            | 5     |
 * |                                 | yyyyy   | ...                               | 3,5   |
 * | Local week-numbering year       | Y       | 44, 1, 1900, 2017                 | 5     |
 * |                                 | Yo      | 44th, 1st, 1900th, 2017th         | 5,7   |
 * |                                 | YY      | 44, 01, 00, 17                    | 5,8   |
 * |                                 | YYY     | 044, 001, 1900, 2017              | 5     |
 * |                                 | YYYY    | 0044, 0001, 1900, 2017            | 5,8   |
 * |                                 | YYYYY   | ...                               | 3,5   |
 * | ISO week-numbering year         | R       | -43, 0, 1, 1900, 2017             | 5,7   |
 * |                                 | RR      | -43, 00, 01, 1900, 2017           | 5,7   |
 * |                                 | RRR     | -043, 000, 001, 1900, 2017        | 5,7   |
 * |                                 | RRRR    | -0043, 0000, 0001, 1900, 2017     | 5,7   |
 * |                                 | RRRRR   | ...                               | 3,5,7 |
 * | Extended year                   | u       | -43, 0, 1, 1900, 2017             | 5     |
 * |                                 | uu      | -43, 01, 1900, 2017               | 5     |
 * |                                 | uuu     | -043, 001, 1900, 2017             | 5     |
 * |                                 | uuuu    | -0043, 0001, 1900, 2017           | 5     |
 * |                                 | uuuuu   | ...                               | 3,5   |
 * | Quarter (formatting)            | Q       | 1, 2, 3, 4                        |       |
 * |                                 | Qo      | 1st, 2nd, 3rd, 4th                | 7     |
 * |                                 | QQ      | 01, 02, 03, 04                    |       |
 * |                                 | QQQ     | Q1, Q2, Q3, Q4                    |       |
 * |                                 | QQQQ    | 1st quarter, 2nd quarter, ...     | 2     |
 * |                                 | QQQQQ   | 1, 2, 3, 4                        | 4     |
 * | Quarter (stand-alone)           | q       | 1, 2, 3, 4                        |       |
 * |                                 | qo      | 1st, 2nd, 3rd, 4th                | 7     |
 * |                                 | qq      | 01, 02, 03, 04                    |       |
 * |                                 | qqq     | Q1, Q2, Q3, Q4                    |       |
 * |                                 | qqqq    | 1st quarter, 2nd quarter, ...     | 2     |
 * |                                 | qqqqq   | 1, 2, 3, 4                        | 4     |
 * | Month (formatting)              | M       | 1, 2, ..., 12                     |       |
 * |                                 | Mo      | 1st, 2nd, ..., 12th               | 7     |
 * |                                 | MM      | 01, 02, ..., 12                   |       |
 * |                                 | MMM     | Jan, Feb, ..., Dec                |       |
 * |                                 | MMMM    | January, February, ..., December  | 2     |
 * |                                 | MMMMM   | J, F, ..., D                      |       |
 * | Month (stand-alone)             | L       | 1, 2, ..., 12                     |       |
 * |                                 | Lo      | 1st, 2nd, ..., 12th               | 7     |
 * |                                 | LL      | 01, 02, ..., 12                   |       |
 * |                                 | LLL     | Jan, Feb, ..., Dec                |       |
 * |                                 | LLLL    | January, February, ..., December  | 2     |
 * |                                 | LLLLL   | J, F, ..., D                      |       |
 * | Local week of year              | w       | 1, 2, ..., 53                     |       |
 * |                                 | wo      | 1st, 2nd, ..., 53th               | 7     |
 * |                                 | ww      | 01, 02, ..., 53                   |       |
 * | ISO week of year                | I       | 1, 2, ..., 53                     | 7     |
 * |                                 | Io      | 1st, 2nd, ..., 53th               | 7     |
 * |                                 | II      | 01, 02, ..., 53                   | 7     |
 * | Day of month                    | d       | 1, 2, ..., 31                     |       |
 * |                                 | do      | 1st, 2nd, ..., 31st               | 7     |
 * |                                 | dd      | 01, 02, ..., 31                   |       |
 * | Day of year                     | D       | 1, 2, ..., 365, 366               | 9     |
 * |                                 | Do      | 1st, 2nd, ..., 365th, 366th       | 7     |
 * |                                 | DD      | 01, 02, ..., 365, 366             | 9     |
 * |                                 | DDD     | 001, 002, ..., 365, 366           |       |
 * |                                 | DDDD    | ...                               | 3     |
 * | Day of week (formatting)        | E..EEE  | Mon, Tue, Wed, ..., Sun           |       |
 * |                                 | EEEE    | Monday, Tuesday, ..., Sunday      | 2     |
 * |                                 | EEEEE   | M, T, W, T, F, S, S               |       |
 * |                                 | EEEEEE  | Mo, Tu, We, Th, Fr, Sa, Su        |       |
 * | ISO day of week (formatting)    | i       | 1, 2, 3, ..., 7                   | 7     |
 * |                                 | io      | 1st, 2nd, ..., 7th                | 7     |
 * |                                 | ii      | 01, 02, ..., 07                   | 7     |
 * |                                 | iii     | Mon, Tue, Wed, ..., Sun           | 7     |
 * |                                 | iiii    | Monday, Tuesday, ..., Sunday      | 2,7   |
 * |                                 | iiiii   | M, T, W, T, F, S, S               | 7     |
 * |                                 | iiiiii  | Mo, Tu, We, Th, Fr, Sa, Su        | 7     |
 * | Local day of week (formatting)  | e       | 2, 3, 4, ..., 1                   |       |
 * |                                 | eo      | 2nd, 3rd, ..., 1st                | 7     |
 * |                                 | ee      | 02, 03, ..., 01                   |       |
 * |                                 | eee     | Mon, Tue, Wed, ..., Sun           |       |
 * |                                 | eeee    | Monday, Tuesday, ..., Sunday      | 2     |
 * |                                 | eeeee   | M, T, W, T, F, S, S               |       |
 * |                                 | eeeeee  | Mo, Tu, We, Th, Fr, Sa, Su        |       |
 * | Local day of week (stand-alone) | c       | 2, 3, 4, ..., 1                   |       |
 * |                                 | co      | 2nd, 3rd, ..., 1st                | 7     |
 * |                                 | cc      | 02, 03, ..., 01                   |       |
 * |                                 | ccc     | Mon, Tue, Wed, ..., Sun           |       |
 * |                                 | cccc    | Monday, Tuesday, ..., Sunday      | 2     |
 * |                                 | ccccc   | M, T, W, T, F, S, S               |       |
 * |                                 | cccccc  | Mo, Tu, We, Th, Fr, Sa, Su        |       |
 * | AM, PM                          | a..aa   | AM, PM                            |       |
 * |                                 | aaa     | am, pm                            |       |
 * |                                 | aaaa    | a.m., p.m.                        | 2     |
 * |                                 | aaaaa   | a, p                              |       |
 * | AM, PM, noon, midnight          | b..bb   | AM, PM, noon, midnight            |       |
 * |                                 | bbb     | am, pm, noon, midnight            |       |
 * |                                 | bbbb    | a.m., p.m., noon, midnight        | 2     |
 * |                                 | bbbbb   | a, p, n, mi                       |       |
 * | Flexible day period             | B..BBB  | at night, in the morning, ...     |       |
 * |                                 | BBBB    | at night, in the morning, ...     | 2     |
 * |                                 | BBBBB   | at night, in the morning, ...     |       |
 * | Hour [1-12]                     | h       | 1, 2, ..., 11, 12                 |       |
 * |                                 | ho      | 1st, 2nd, ..., 11th, 12th         | 7     |
 * |                                 | hh      | 01, 02, ..., 11, 12               |       |
 * | Hour [0-23]                     | H       | 0, 1, 2, ..., 23                  |       |
 * |                                 | Ho      | 0th, 1st, 2nd, ..., 23rd          | 7     |
 * |                                 | HH      | 00, 01, 02, ..., 23               |       |
 * | Hour [0-11]                     | K       | 1, 2, ..., 11, 0                  |       |
 * |                                 | Ko      | 1st, 2nd, ..., 11th, 0th          | 7     |
 * |                                 | KK      | 01, 02, ..., 11, 00               |       |
 * | Hour [1-24]                     | k       | 24, 1, 2, ..., 23                 |       |
 * |                                 | ko      | 24th, 1st, 2nd, ..., 23rd         | 7     |
 * |                                 | kk      | 24, 01, 02, ..., 23               |       |
 * | Minute                          | m       | 0, 1, ..., 59                     |       |
 * |                                 | mo      | 0th, 1st, ..., 59th               | 7     |
 * |                                 | mm      | 00, 01, ..., 59                   |       |
 * | Second                          | s       | 0, 1, ..., 59                     |       |
 * |                                 | so      | 0th, 1st, ..., 59th               | 7     |
 * |                                 | ss      | 00, 01, ..., 59                   |       |
 * | Fraction of second              | S       | 0, 1, ..., 9                      |       |
 * |                                 | SS      | 00, 01, ..., 99                   |       |
 * |                                 | SSS     | 000, 001, ..., 999                |       |
 * |                                 | SSSS    | ...                               | 3     |
 * | Timezone (ISO-8601 w/ Z)        | X       | -08, +0530, Z                     |       |
 * |                                 | XX      | -0800, +0530, Z                   |       |
 * |                                 | XXX     | -08:00, +05:30, Z                 |       |
 * |                                 | XXXX    | -0800, +0530, Z, +123456          | 2     |
 * |                                 | XXXXX   | -08:00, +05:30, Z, +12:34:56      |       |
 * | Timezone (ISO-8601 w/o Z)       | x       | -08, +0530, +00                   |       |
 * |                                 | xx      | -0800, +0530, +0000               |       |
 * |                                 | xxx     | -08:00, +05:30, +00:00            | 2     |
 * |                                 | xxxx    | -0800, +0530, +0000, +123456      |       |
 * |                                 | xxxxx   | -08:00, +05:30, +00:00, +12:34:56 |       |
 * | Timezone (GMT)                  | O...OOO | GMT-8, GMT+5:30, GMT+0            |       |
 * |                                 | OOOO    | GMT-08:00, GMT+05:30, GMT+00:00   | 2     |
 * | Timezone (specific non-locat.)  | z...zzz | GMT-8, GMT+5:30, GMT+0            | 6     |
 * |                                 | zzzz    | GMT-08:00, GMT+05:30, GMT+00:00   | 2,6   |
 * | Seconds timestamp               | t       | 512969520                         | 7     |
 * |                                 | tt      | ...                               | 3,7   |
 * | Milliseconds timestamp          | T       | 512969520900                      | 7     |
 * |                                 | TT      | ...                               | 3,7   |
 * | Long localized date             | P       | 04/29/1453                        | 7     |
 * |                                 | PP      | Apr 29, 1453                      | 7     |
 * |                                 | PPP     | April 29th, 1453                  | 7     |
 * |                                 | PPPP    | Friday, April 29th, 1453          | 2,7   |
 * | Long localized time             | p       | 12:00 AM                          | 7     |
 * |                                 | pp      | 12:00:00 AM                       | 7     |
 * |                                 | ppp     | 12:00:00 AM GMT+2                 | 7     |
 * |                                 | pppp    | 12:00:00 AM GMT+02:00             | 2,7   |
 * | Combination of date and time    | Pp      | 04/29/1453, 12:00 AM              | 7     |
 * |                                 | PPpp    | Apr 29, 1453, 12:00:00 AM         | 7     |
 * |                                 | PPPppp  | April 29th, 1453 at ...           | 7     |
 * |                                 | PPPPpppp| Friday, April 29th, 1453 at ...   | 2,7   |
 * Notes:
 * 1. "Formatting" units (e.g. formatting quarter) in the default en-US locale
 *    are the same as "stand-alone" units, but are different in some languages.
 *    "Formatting" units are declined according to the rules of the language
 *    in the context of a date. "Stand-alone" units are always nominative singular:
 *
 *    `format(new Date(2017, 10, 6), 'do LLLL', {locale: cs}) //=> '6. listopad'`
 *
 *    `format(new Date(2017, 10, 6), 'do MMMM', {locale: cs}) //=> '6. listopadu'`
 *
 * 2. Any sequence of the identical letters is a pattern, unless it is escaped by
 *    the single quote characters (see below).
 *    If the sequence is longer than listed in table (e.g. `EEEEEEEEEEE`)
 *    the output will be the same as default pattern for this unit, usually
 *    the longest one (in case of ISO weekdays, `EEEE`). Default patterns for units
 *    are marked with "2" in the last column of the table.
 *
 *    `format(new Date(2017, 10, 6), 'MMM') //=> 'Nov'`
 *
 *    `format(new Date(2017, 10, 6), 'MMMM') //=> 'November'`
 *
 *    `format(new Date(2017, 10, 6), 'MMMMM') //=> 'N'`
 *
 *    `format(new Date(2017, 10, 6), 'MMMMMM') //=> 'November'`
 *
 *    `format(new Date(2017, 10, 6), 'MMMMMMM') //=> 'November'`
 *
 * 3. Some patterns could be unlimited length (such as `yyyyyyyy`).
 *    The output will be padded with zeros to match the length of the pattern.
 *
 *    `format(new Date(2017, 10, 6), 'yyyyyyyy') //=> '00002017'`
 *
 * 4. `QQQQQ` and `qqqqq` could be not strictly numerical in some locales.
 *    These tokens represent the shortest form of the quarter.
 *
 * 5. The main difference between `y` and `u` patterns are B.C. years:
 *
 *    | Year | `y` | `u` |
 *    |------|-----|-----|
 *    | AC 1 |   1 |   1 |
 *    | BC 1 |   1 |   0 |
 *    | BC 2 |   2 |  -1 |
 *
 *    Also `yy` always returns the last two digits of a year,
 *    while `uu` pads single digit years to 2 characters and returns other years unchanged:
 *
 *    | Year | `yy` | `uu` |
 *    |------|------|------|
 *    | 1    |   01 |   01 |
 *    | 14   |   14 |   14 |
 *    | 376  |   76 |  376 |
 *    | 1453 |   53 | 1453 |
 *
 *    The same difference is true for local and ISO week-numbering years (`Y` and `R`),
 *    except local week-numbering years are dependent on `options.weekStartsOn`
 *    and `options.firstWeekContainsDate` (compare [getISOWeekYear](https://date-fns.org/docs/getISOWeekYear)
 *    and [getWeekYear](https://date-fns.org/docs/getWeekYear)).
 *
 * 6. Specific non-location timezones are currently unavailable in `date-fns`,
 *    so right now these tokens fall back to GMT timezones.
 *
 * 7. These patterns are not in the Unicode Technical Standard #35:
 *    - `i`: ISO day of week
 *    - `I`: ISO week of year
 *    - `R`: ISO week-numbering year
 *    - `t`: seconds timestamp
 *    - `T`: milliseconds timestamp
 *    - `o`: ordinal number modifier
 *    - `P`: long localized date
 *    - `p`: long localized time
 *
 * 8. `YY` and `YYYY` tokens represent week-numbering years but they are often confused with years.
 *    You should enable `options.useAdditionalWeekYearTokens` to use them. See: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 *
 * 9. `D` and `DD` tokens represent days of the year but they are often confused with days of the month.
 *    You should enable `options.useAdditionalDayOfYearTokens` to use them. See: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 *
 * @param date - The original date
 * @param format - The string of tokens
 * @param options - An object with options
 *
 * @returns The formatted date string
 *
 * @throws `date` must not be Invalid Date
 * @throws `options.locale` must contain `localize` property
 * @throws `options.locale` must contain `formatLong` property
 * @throws use `yyyy` instead of `YYYY` for formatting years using [format provided] to the input [input provided]; see: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 * @throws use `yy` instead of `YY` for formatting years using [format provided] to the input [input provided]; see: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 * @throws use `d` instead of `D` for formatting days of the month using [format provided] to the input [input provided]; see: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 * @throws use `dd` instead of `DD` for formatting days of the month using [format provided] to the input [input provided]; see: https://github.com/date-fns/date-fns/blob/master/docs/unicodeTokens.md
 * @throws format string contains an unescaped latin alphabet character
 *
 * @example
 * // Represent 11 February 2014 in middle-endian format:
 * const result = format(new Date(2014, 1, 11), 'MM/dd/yyyy')
 * //=> '02/11/2014'
 *
 * @example
 * // Represent 2 July 2014 in Esperanto:
 * import { eoLocale } from 'date-fns/locale/eo'
 * const result = format(new Date(2014, 6, 2), "do 'de' MMMM yyyy", {
 *   locale: eoLocale
 * })
 * //=> '2-a de julio 2014'
 *
 * @example
 * // Escape string by single quote characters:
 * const result = format(new Date(2014, 6, 2, 15), "h 'o''clock'")
 * //=> "3 o'clock"
 */
function format(date, formatStr, options) {
  const defaultOptions = getDefaultOptions();
  const locale = options?.locale ?? defaultOptions.locale ?? enUS$1;

  const firstWeekContainsDate =
    options?.firstWeekContainsDate ??
    options?.locale?.options?.firstWeekContainsDate ??
    defaultOptions.firstWeekContainsDate ??
    defaultOptions.locale?.options?.firstWeekContainsDate ??
    1;

  const weekStartsOn =
    options?.weekStartsOn ??
    options?.locale?.options?.weekStartsOn ??
    defaultOptions.weekStartsOn ??
    defaultOptions.locale?.options?.weekStartsOn ??
    0;

  const originalDate = toDate(date, options?.in);

  if (!isValid(originalDate)) {
    throw new RangeError("Invalid time value");
  }

  let parts = formatStr
    .match(longFormattingTokensRegExp)
    .map((substring) => {
      const firstCharacter = substring[0];
      if (firstCharacter === "p" || firstCharacter === "P") {
        const longFormatter = longFormatters[firstCharacter];
        return longFormatter(substring, locale.formatLong);
      }
      return substring;
    })
    .join("")
    .match(formattingTokensRegExp)
    .map((substring) => {
      // Replace two single quote characters with one single quote character
      if (substring === "''") {
        return { isToken: false, value: "'" };
      }

      const firstCharacter = substring[0];
      if (firstCharacter === "'") {
        return { isToken: false, value: cleanEscapedString(substring) };
      }

      if (formatters[firstCharacter]) {
        return { isToken: true, value: substring };
      }

      if (firstCharacter.match(unescapedLatinCharacterRegExp)) {
        throw new RangeError(
          "Format string contains an unescaped latin alphabet character `" +
            firstCharacter +
            "`",
        );
      }

      return { isToken: false, value: substring };
    });

  // invoke localize preprocessor (only for french locales at the moment)
  if (locale.localize.preprocessor) {
    parts = locale.localize.preprocessor(originalDate, parts);
  }

  const formatterOptions = {
    firstWeekContainsDate,
    weekStartsOn,
    locale,
  };

  return parts
    .map((part) => {
      if (!part.isToken) return part.value;

      const token = part.value;

      if (
        (!options?.useAdditionalWeekYearTokens &&
          isProtectedWeekYearToken(token)) ||
        (!options?.useAdditionalDayOfYearTokens &&
          isProtectedDayOfYearToken(token))
      ) {
        warnOrThrowProtectedError(token, formatStr, String(date));
      }

      const formatter = formatters[token[0]];
      return formatter(originalDate, token, locale.localize, formatterOptions);
    })
    .join("");
}

function cleanEscapedString(input) {
  const matched = input.match(escapedStringRegExp);

  if (!matched) {
    return input;
  }

  return matched[1].replace(doubleQuoteRegExp, "'");
}

/**
 * The {@link getDaysInMonth} function options.
 */

/**
 * @name getDaysInMonth
 * @category Month Helpers
 * @summary Get the number of days in a month of the given date.
 *
 * @description
 * Get the number of days in a month of the given date, considering the context if provided.
 *
 * @param date - The given date
 * @param options - An object with options
 *
 * @returns The number of days in a month
 *
 * @example
 * // How many days are in February 2000?
 * const result = getDaysInMonth(new Date(2000, 1))
 * //=> 29
 */
function getDaysInMonth(date, options) {
  const _date = toDate(date, options?.in);
  const year = _date.getFullYear();
  const monthIndex = _date.getMonth();
  const lastDayOfMonth = constructFrom(_date, 0);
  lastDayOfMonth.setFullYear(year, monthIndex + 1, 0);
  lastDayOfMonth.setHours(0, 0, 0, 0);
  return lastDayOfMonth.getDate();
}

/**
 * The {@link getMonth} function options.
 */

/**
 * @name getMonth
 * @category Month Helpers
 * @summary Get the month of the given date.
 *
 * @description
 * Get the month of the given date.
 *
 * @param date - The given date
 * @param options - An object with options
 *
 * @returns The month index (0-11)
 *
 * @example
 * // Which month is 29 February 2012?
 * const result = getMonth(new Date(2012, 1, 29))
 * //=> 1
 */
function getMonth(date, options) {
  return toDate(date, options?.in).getMonth();
}

/**
 * The {@link getYear} function options.
 */

/**
 * @name getYear
 * @category Year Helpers
 * @summary Get the year of the given date.
 *
 * @description
 * Get the year of the given date.
 *
 * @param date - The given date
 * @param options - An object with options
 *
 * @returns The year
 *
 * @example
 * // Which year is 2 July 2014?
 * const result = getYear(new Date(2014, 6, 2))
 * //=> 2014
 */
function getYear(date, options) {
  return toDate(date, options?.in).getFullYear();
}

/**
 * @name isAfter
 * @category Common Helpers
 * @summary Is the first date after the second one?
 *
 * @description
 * Is the first date after the second one?
 *
 * @param date - The date that should be after the other one to return true
 * @param dateToCompare - The date to compare with
 *
 * @returns The first date is after the second date
 *
 * @example
 * // Is 10 July 1989 after 11 February 1987?
 * const result = isAfter(new Date(1989, 6, 10), new Date(1987, 1, 11))
 * //=> true
 */
function isAfter(date, dateToCompare) {
  return +toDate(date) > +toDate(dateToCompare);
}

/**
 * @name isBefore
 * @category Common Helpers
 * @summary Is the first date before the second one?
 *
 * @description
 * Is the first date before the second one?
 *
 * @param date - The date that should be before the other one to return true
 * @param dateToCompare - The date to compare with
 *
 * @returns The first date is before the second date
 *
 * @example
 * // Is 10 July 1989 before 11 February 1987?
 * const result = isBefore(new Date(1989, 6, 10), new Date(1987, 1, 11))
 * //=> false
 */
function isBefore(date, dateToCompare) {
  return +toDate(date) < +toDate(dateToCompare);
}

/**
 * The {@link isSameMonth} function options.
 */

/**
 * @name isSameMonth
 * @category Month Helpers
 * @summary Are the given dates in the same month (and year)?
 *
 * @description
 * Are the given dates in the same month (and year)?
 *
 * @param laterDate - The first date to check
 * @param earlierDate - The second date to check
 * @param options - An object with options
 *
 * @returns The dates are in the same month (and year)
 *
 * @example
 * // Are 2 September 2014 and 25 September 2014 in the same month?
 * const result = isSameMonth(new Date(2014, 8, 2), new Date(2014, 8, 25))
 * //=> true
 *
 * @example
 * // Are 2 September 2014 and 25 September 2015 in the same month?
 * const result = isSameMonth(new Date(2014, 8, 2), new Date(2015, 8, 25))
 * //=> false
 */
function isSameMonth(laterDate, earlierDate, options) {
  const [laterDate_, earlierDate_] = normalizeDates(
    options?.in,
    laterDate,
    earlierDate,
  );
  return (
    laterDate_.getFullYear() === earlierDate_.getFullYear() &&
    laterDate_.getMonth() === earlierDate_.getMonth()
  );
}

/**
 * The {@link isSameYear} function options.
 */

/**
 * @name isSameYear
 * @category Year Helpers
 * @summary Are the given dates in the same year?
 *
 * @description
 * Are the given dates in the same year?
 *
 * @param laterDate - The first date to check
 * @param earlierDate - The second date to check
 * @param options - An object with options
 *
 * @returns The dates are in the same year
 *
 * @example
 * // Are 2 September 2014 and 25 September 2014 in the same year?
 * const result = isSameYear(new Date(2014, 8, 2), new Date(2014, 8, 25))
 * //=> true
 */
function isSameYear(laterDate, earlierDate, options) {
  const [laterDate_, earlierDate_] = normalizeDates(
    options?.in,
    laterDate,
    earlierDate,
  );
  return laterDate_.getFullYear() === earlierDate_.getFullYear();
}

/**
 * The {@link setMonth} function options.
 */

/**
 * @name setMonth
 * @category Month Helpers
 * @summary Set the month to the given date.
 *
 * @description
 * Set the month to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The date to be changed
 * @param month - The month index to set (0-11)
 * @param options - The options
 *
 * @returns The new date with the month set
 *
 * @example
 * // Set February to 1 September 2014:
 * const result = setMonth(new Date(2014, 8, 1), 1)
 * //=> Sat Feb 01 2014 00:00:00
 */
function setMonth(date, month, options) {
  const _date = toDate(date, options?.in);
  const year = _date.getFullYear();
  const day = _date.getDate();

  const midMonth = constructFrom(date, 0);
  midMonth.setFullYear(year, month, 15);
  midMonth.setHours(0, 0, 0, 0);
  const daysInMonth = getDaysInMonth(midMonth);

  // Set the earlier date, allows to wrap Jan 31 to Feb 28
  _date.setMonth(month, Math.min(day, daysInMonth));
  return _date;
}

/**
 * The {@link setYear} function options.
 */

/**
 * @name setYear
 * @category Year Helpers
 * @summary Set the year to the given date.
 *
 * @description
 * Set the year to the given date.
 *
 * @typeParam DateType - The `Date` type, the function operates on. Gets inferred from passed arguments. Allows to use extensions like [`UTCDate`](https://github.com/date-fns/utc).
 * @typeParam ResultDate - The result `Date` type, it is the type returned from the context function if it is passed, or inferred from the arguments.
 *
 * @param date - The date to be changed
 * @param year - The year of the new date
 * @param options - An object with options.
 *
 * @returns The new date with the year set
 *
 * @example
 * // Set year 2013 to 1 September 2014:
 * const result = setYear(new Date(2014, 8, 1), 2013)
 * //=> Sun Sep 01 2013 00:00:00
 */
function setYear(date, year, options) {
  const date_ = toDate(date, options?.in);

  // Check if date is Invalid Date because Date.prototype.setFullYear ignores the value of Invalid Date
  if (isNaN(+date_)) return constructFrom(date, NaN);

  date_.setFullYear(year);
  return date_;
}

const FIVE_WEEKS = 5;
const FOUR_WEEKS = 4;
/**
 * Returns the number of weeks to display in the broadcast calendar for a given
 * month.
 *
 * The broadcast calendar may have either 4 or 5 weeks in a month, depending on
 * the start and end dates of the broadcast weeks.
 *
 * @since 9.4.0
 * @param month The month for which to calculate the number of weeks.
 * @param dateLib The date library to use for date manipulation.
 * @returns The number of weeks in the broadcast calendar (4 or 5).
 */
function getBroadcastWeeksInMonth(month, dateLib) {
    // Get the first day of the month
    const firstDayOfMonth = dateLib.startOfMonth(month);
    // Get the day of the week for the first day of the month (1-7, where 1 is Monday)
    const firstDayOfWeek = firstDayOfMonth.getDay() > 0 ? firstDayOfMonth.getDay() : 7;
    const broadcastStartDate = dateLib.addDays(month, -firstDayOfWeek + 1);
    const lastDateOfLastWeek = dateLib.addDays(broadcastStartDate, FIVE_WEEKS * 7 - 1);
    const numberOfWeeks = dateLib.getMonth(month) === dateLib.getMonth(lastDateOfLastWeek)
        ? FIVE_WEEKS
        : FOUR_WEEKS;
    return numberOfWeeks;
}

/**
 * Returns the start date of the week in the broadcast calendar.
 *
 * The broadcast week starts on Monday. If the first day of the month is not a
 * Monday, this function calculates the previous Monday as the start of the
 * broadcast week.
 *
 * @since 9.4.0
 * @param date The date for which to calculate the start of the broadcast week.
 * @param dateLib The date library to use for date manipulation.
 * @returns The start date of the broadcast week.
 */
function startOfBroadcastWeek(date, dateLib) {
    const firstOfMonth = dateLib.startOfMonth(date);
    const dayOfWeek = firstOfMonth.getDay();
    if (dayOfWeek === 1) {
        return firstOfMonth;
    }
    else if (dayOfWeek === 0) {
        return dateLib.addDays(firstOfMonth, -1 * 6);
    }
    else {
        return dateLib.addDays(firstOfMonth, -1 * (dayOfWeek - 1));
    }
}

/**
 * Returns the end date of the week in the broadcast calendar.
 *
 * The broadcast week ends on the last day of the last broadcast week for the
 * given date.
 *
 * @since 9.4.0
 * @param date The date for which to calculate the end of the broadcast week.
 * @param dateLib The date library to use for date manipulation.
 * @returns The end date of the broadcast week.
 */
function endOfBroadcastWeek(date, dateLib) {
    const startDate = startOfBroadcastWeek(date, dateLib);
    const numberOfWeeks = getBroadcastWeeksInMonth(date, dateLib);
    const endDate = dateLib.addDays(startDate, numberOfWeeks * 7 - 1);
    return endDate;
}

/** English (United States) locale extended with DayPicker-specific translations. */
const enUS = {
    ...enUS$1,
    labels: {
        labelDayButton: (date, modifiers, options, dateLib) => {
            let formatDate;
            if (dateLib && typeof dateLib.format === "function") {
                formatDate = dateLib.format.bind(dateLib);
            }
            else {
                formatDate = (d, pattern) => format(d, pattern, { locale: enUS$1, ...options });
            }
            let label = formatDate(date, "PPPP");
            if (modifiers.today)
                label = `Today, ${label}`;
            if (modifiers.selected)
                label = `${label}, selected`;
            return label;
        },
        labelMonthDropdown: "Choose the Month",
        labelNext: "Go to the Next Month",
        labelPrevious: "Go to the Previous Month",
        labelWeekNumber: (weekNumber) => `Week ${weekNumber}`,
        labelYearDropdown: "Choose the Year",
        labelGrid: (date, options, dateLib) => {
            let formatDate;
            if (dateLib && typeof dateLib.format === "function") {
                formatDate = dateLib.format.bind(dateLib);
            }
            else {
                formatDate = (d, pattern) => format(d, pattern, { locale: enUS$1, ...options });
            }
            return formatDate(date, "LLLL yyyy");
        },
        labelGridcell: (date, modifiers, options, dateLib) => {
            let formatDate;
            if (dateLib && typeof dateLib.format === "function") {
                formatDate = dateLib.format.bind(dateLib);
            }
            else {
                formatDate = (d, pattern) => format(d, pattern, { locale: enUS$1, ...options });
            }
            let label = formatDate(date, "PPPP");
            if (modifiers?.today) {
                label = `Today, ${label}`;
            }
            return label;
        },
        labelNav: "Navigation bar",
        labelWeekNumberHeader: "Week Number",
        labelWeekday: (date, options, dateLib) => {
            let formatDate;
            if (dateLib && typeof dateLib.format === "function") {
                formatDate = dateLib.format.bind(dateLib);
            }
            else {
                formatDate = (d, pattern) => format(d, pattern, { locale: enUS$1, ...options });
            }
            return formatDate(date, "cccc");
        },
    },
};

/**
 * A wrapper class around [date-fns](http://date-fns.org) that provides utility
 * methods for date manipulation and formatting.
 *
 * @since 9.2.0
 * @example
 *   const dateLib = new DateLib({ locale: es });
 *   const newDate = dateLib.addDays(new Date(), 5);
 */
class DateLib {
    /**
     * Creates an instance of `DateLib`.
     *
     * @param options Configuration options for the date library.
     * @param overrides Custom overrides for the date library functions.
     */
    constructor(options, overrides) {
        /**
         * Reference to the built-in Date constructor.
         *
         * @deprecated Use `newDate()` or `today()`.
         */
        this.Date = Date;
        /**
         * Creates a new `Date` object representing today's date.
         *
         * @since 9.5.0
         * @returns A `Date` object for today's date.
         */
        this.today = () => {
            if (this.overrides?.today) {
                return this.overrides.today();
            }
            if (this.options.timeZone) {
                return TZDate.tz(this.options.timeZone);
            }
            return new this.Date();
        };
        /**
         * Creates a new `Date` object with the specified year, month, and day.
         *
         * @since 9.5.0
         * @param year The year.
         * @param monthIndex The month (0-11).
         * @param date The day of the month.
         * @returns A new `Date` object.
         */
        this.newDate = (year, monthIndex, date) => {
            if (this.overrides?.newDate) {
                return this.overrides.newDate(year, monthIndex, date);
            }
            if (this.options.timeZone) {
                return new TZDate(year, monthIndex, date, this.options.timeZone);
            }
            return new Date(year, monthIndex, date);
        };
        /**
         * Adds the specified number of days to the given date.
         *
         * @param date The date to add days to.
         * @param amount The number of days to add.
         * @returns The new date with the days added.
         */
        this.addDays = (date, amount) => {
            return this.overrides?.addDays
                ? this.overrides.addDays(date, amount)
                : addDays(date, amount);
        };
        /**
         * Adds the specified number of months to the given date.
         *
         * @param date The date to add months to.
         * @param amount The number of months to add.
         * @returns The new date with the months added.
         */
        this.addMonths = (date, amount) => {
            return this.overrides?.addMonths
                ? this.overrides.addMonths(date, amount)
                : addMonths(date, amount);
        };
        /**
         * Adds the specified number of weeks to the given date.
         *
         * @param date The date to add weeks to.
         * @param amount The number of weeks to add.
         * @returns The new date with the weeks added.
         */
        this.addWeeks = (date, amount) => {
            return this.overrides?.addWeeks
                ? this.overrides.addWeeks(date, amount)
                : addWeeks(date, amount);
        };
        /**
         * Adds the specified number of years to the given date.
         *
         * @param date The date to add years to.
         * @param amount The number of years to add.
         * @returns The new date with the years added.
         */
        this.addYears = (date, amount) => {
            return this.overrides?.addYears
                ? this.overrides.addYears(date, amount)
                : addYears(date, amount);
        };
        /**
         * Returns the number of calendar days between the given dates.
         *
         * @param dateLeft The later date.
         * @param dateRight The earlier date.
         * @returns The number of calendar days between the dates.
         */
        this.differenceInCalendarDays = (dateLeft, dateRight) => {
            return this.overrides?.differenceInCalendarDays
                ? this.overrides.differenceInCalendarDays(dateLeft, dateRight)
                : differenceInCalendarDays(dateLeft, dateRight);
        };
        /**
         * Returns the number of calendar months between the given dates.
         *
         * @param dateLeft The later date.
         * @param dateRight The earlier date.
         * @returns The number of calendar months between the dates.
         */
        this.differenceInCalendarMonths = (dateLeft, dateRight) => {
            return this.overrides?.differenceInCalendarMonths
                ? this.overrides.differenceInCalendarMonths(dateLeft, dateRight)
                : differenceInCalendarMonths(dateLeft, dateRight);
        };
        /**
         * Returns the months between the given dates.
         *
         * @param interval The interval to get the months for.
         */
        this.eachMonthOfInterval = (interval) => {
            return this.overrides?.eachMonthOfInterval
                ? this.overrides.eachMonthOfInterval(interval)
                : eachMonthOfInterval(interval);
        };
        /**
         * Returns the years between the given dates.
         *
         * @since 9.11.1
         * @param interval The interval to get the years for.
         * @returns The array of years in the interval.
         */
        this.eachYearOfInterval = (interval) => {
            const years = this.overrides?.eachYearOfInterval
                ? this.overrides.eachYearOfInterval(interval)
                : eachYearOfInterval(interval);
            // Remove duplicates that may happen across DST transitions (e.g., "America/Sao_Paulo")
            // See https://github.com/date-fns/tz/issues/72
            const uniqueYears = new Set(years.map((d) => this.getYear(d)));
            if (uniqueYears.size === years.length) {
                // No duplicates, return as is
                return years;
            }
            // Rebuild the array to ensure one date per year
            const yearsArray = [];
            uniqueYears.forEach((y) => {
                yearsArray.push(new Date(y, 0, 1));
            });
            return yearsArray;
        };
        /**
         * Returns the end of the broadcast week for the given date.
         *
         * @param date The original date.
         * @returns The end of the broadcast week.
         */
        this.endOfBroadcastWeek = (date) => {
            return this.overrides?.endOfBroadcastWeek
                ? this.overrides.endOfBroadcastWeek(date)
                : endOfBroadcastWeek(date, this);
        };
        /**
         * Returns the end of the ISO week for the given date.
         *
         * @param date The original date.
         * @returns The end of the ISO week.
         */
        this.endOfISOWeek = (date) => {
            return this.overrides?.endOfISOWeek
                ? this.overrides.endOfISOWeek(date)
                : endOfISOWeek(date);
        };
        /**
         * Returns the end of the month for the given date.
         *
         * @param date The original date.
         * @returns The end of the month.
         */
        this.endOfMonth = (date) => {
            return this.overrides?.endOfMonth
                ? this.overrides.endOfMonth(date)
                : endOfMonth(date);
        };
        /**
         * Returns the end of the week for the given date.
         *
         * @param date The original date.
         * @returns The end of the week.
         */
        this.endOfWeek = (date, options) => {
            return this.overrides?.endOfWeek
                ? this.overrides.endOfWeek(date, options)
                : endOfWeek(date, this.options);
        };
        /**
         * Returns the end of the year for the given date.
         *
         * @param date The original date.
         * @returns The end of the year.
         */
        this.endOfYear = (date) => {
            return this.overrides?.endOfYear
                ? this.overrides.endOfYear(date)
                : endOfYear(date);
        };
        /**
         * Formats the given date using the specified format string.
         *
         * @param date The date to format.
         * @param formatStr The format string.
         * @returns The formatted date string.
         */
        this.format = (date, formatStr, _options) => {
            const formatted = this.overrides?.format
                ? this.overrides.format(date, formatStr, this.options)
                : format(date, formatStr, this.options);
            if (this.options.numerals && this.options.numerals !== "latn") {
                return this.replaceDigits(formatted);
            }
            return formatted;
        };
        /**
         * Returns the ISO week number for the given date.
         *
         * @param date The date to get the ISO week number for.
         * @returns The ISO week number.
         */
        this.getISOWeek = (date) => {
            return this.overrides?.getISOWeek
                ? this.overrides.getISOWeek(date)
                : getISOWeek(date);
        };
        /**
         * Returns the month of the given date.
         *
         * @param date The date to get the month for.
         * @returns The month.
         */
        this.getMonth = (date, _options) => {
            return this.overrides?.getMonth
                ? this.overrides.getMonth(date, this.options)
                : getMonth(date, this.options);
        };
        /**
         * Returns the year of the given date.
         *
         * @param date The date to get the year for.
         * @returns The year.
         */
        this.getYear = (date, _options) => {
            return this.overrides?.getYear
                ? this.overrides.getYear(date, this.options)
                : getYear(date, this.options);
        };
        /**
         * Returns the local week number for the given date.
         *
         * @param date The date to get the week number for.
         * @returns The week number.
         */
        this.getWeek = (date, _options) => {
            return this.overrides?.getWeek
                ? this.overrides.getWeek(date, this.options)
                : getWeek(date, this.options);
        };
        /**
         * Checks if the first date is after the second date.
         *
         * @param date The date to compare.
         * @param dateToCompare The date to compare with.
         * @returns True if the first date is after the second date.
         */
        this.isAfter = (date, dateToCompare) => {
            return this.overrides?.isAfter
                ? this.overrides.isAfter(date, dateToCompare)
                : isAfter(date, dateToCompare);
        };
        /**
         * Checks if the first date is before the second date.
         *
         * @param date The date to compare.
         * @param dateToCompare The date to compare with.
         * @returns True if the first date is before the second date.
         */
        this.isBefore = (date, dateToCompare) => {
            return this.overrides?.isBefore
                ? this.overrides.isBefore(date, dateToCompare)
                : isBefore(date, dateToCompare);
        };
        /**
         * Checks if the given value is a Date object.
         *
         * @param value The value to check.
         * @returns True if the value is a Date object.
         */
        this.isDate = (value) => {
            return this.overrides?.isDate
                ? this.overrides.isDate(value)
                : isDate(value);
        };
        /**
         * Checks if the given dates are on the same day.
         *
         * @param dateLeft The first date to compare.
         * @param dateRight The second date to compare.
         * @returns True if the dates are on the same day.
         */
        this.isSameDay = (dateLeft, dateRight) => {
            return this.overrides?.isSameDay
                ? this.overrides.isSameDay(dateLeft, dateRight)
                : isSameDay(dateLeft, dateRight);
        };
        /**
         * Checks if the given dates are in the same month.
         *
         * @param dateLeft The first date to compare.
         * @param dateRight The second date to compare.
         * @returns True if the dates are in the same month.
         */
        this.isSameMonth = (dateLeft, dateRight) => {
            return this.overrides?.isSameMonth
                ? this.overrides.isSameMonth(dateLeft, dateRight)
                : isSameMonth(dateLeft, dateRight);
        };
        /**
         * Checks if the given dates are in the same year.
         *
         * @param dateLeft The first date to compare.
         * @param dateRight The second date to compare.
         * @returns True if the dates are in the same year.
         */
        this.isSameYear = (dateLeft, dateRight) => {
            return this.overrides?.isSameYear
                ? this.overrides.isSameYear(dateLeft, dateRight)
                : isSameYear(dateLeft, dateRight);
        };
        /**
         * Returns the latest date in the given array of dates.
         *
         * @param dates The array of dates to compare.
         * @returns The latest date.
         */
        this.max = (dates) => {
            return this.overrides?.max ? this.overrides.max(dates) : max(dates);
        };
        /**
         * Returns the earliest date in the given array of dates.
         *
         * @param dates The array of dates to compare.
         * @returns The earliest date.
         */
        this.min = (dates) => {
            return this.overrides?.min ? this.overrides.min(dates) : min(dates);
        };
        /**
         * Sets the month of the given date.
         *
         * @param date The date to set the month on.
         * @param month The month to set (0-11).
         * @returns The new date with the month set.
         */
        this.setMonth = (date, month) => {
            return this.overrides?.setMonth
                ? this.overrides.setMonth(date, month)
                : setMonth(date, month);
        };
        /**
         * Sets the year of the given date.
         *
         * @param date The date to set the year on.
         * @param year The year to set.
         * @returns The new date with the year set.
         */
        this.setYear = (date, year) => {
            return this.overrides?.setYear
                ? this.overrides.setYear(date, year)
                : setYear(date, year);
        };
        /**
         * Returns the start of the broadcast week for the given date.
         *
         * @param date The original date.
         * @returns The start of the broadcast week.
         */
        this.startOfBroadcastWeek = (date, _dateLib) => {
            return this.overrides?.startOfBroadcastWeek
                ? this.overrides.startOfBroadcastWeek(date, this)
                : startOfBroadcastWeek(date, this);
        };
        /**
         * Returns the start of the day for the given date.
         *
         * @param date The original date.
         * @returns The start of the day.
         */
        this.startOfDay = (date) => {
            return this.overrides?.startOfDay
                ? this.overrides.startOfDay(date)
                : startOfDay(date);
        };
        /**
         * Returns the start of the ISO week for the given date.
         *
         * @param date The original date.
         * @returns The start of the ISO week.
         */
        this.startOfISOWeek = (date) => {
            return this.overrides?.startOfISOWeek
                ? this.overrides.startOfISOWeek(date)
                : startOfISOWeek(date);
        };
        /**
         * Returns the start of the month for the given date.
         *
         * @param date The original date.
         * @returns The start of the month.
         */
        this.startOfMonth = (date) => {
            return this.overrides?.startOfMonth
                ? this.overrides.startOfMonth(date)
                : startOfMonth(date);
        };
        /**
         * Returns the start of the week for the given date.
         *
         * @param date The original date.
         * @returns The start of the week.
         */
        this.startOfWeek = (date, _options) => {
            return this.overrides?.startOfWeek
                ? this.overrides.startOfWeek(date, this.options)
                : startOfWeek(date, this.options);
        };
        /**
         * Returns the start of the year for the given date.
         *
         * @param date The original date.
         * @returns The start of the year.
         */
        this.startOfYear = (date) => {
            return this.overrides?.startOfYear
                ? this.overrides.startOfYear(date)
                : startOfYear(date);
        };
        this.options = { locale: enUS, ...options };
        this.overrides = overrides;
    }
    /**
     * Generates a mapping of Arabic digits (0-9) to the target numbering system
     * digits.
     *
     * @since 9.5.0
     * @returns A record mapping Arabic digits to the target numerals.
     */
    getDigitMap() {
        const { numerals = "latn" } = this.options;
        // Use Intl.NumberFormat to create a formatter with the specified numbering system
        const formatter = new Intl.NumberFormat("en-US", {
            numberingSystem: numerals,
        });
        // Map Arabic digits (0-9) to the target numerals
        const digitMap = {};
        for (let i = 0; i < 10; i++) {
            digitMap[i.toString()] = formatter.format(i);
        }
        return digitMap;
    }
    /**
     * Replaces Arabic digits in a string with the target numbering system digits.
     *
     * @since 9.5.0
     * @param input The string containing Arabic digits.
     * @returns The string with digits replaced.
     */
    replaceDigits(input) {
        const digitMap = this.getDigitMap();
        return input.replace(/\d/g, (digit) => digitMap[digit] || digit);
    }
    /**
     * Formats a number using the configured numbering system.
     *
     * @since 9.5.0
     * @param value The number to format.
     * @returns The formatted number as a string.
     */
    formatNumber(value) {
        return this.replaceDigits(value.toString());
    }
    /**
     * Returns the preferred ordering for month and year labels for the current
     * locale.
     */
    getMonthYearOrder() {
        const code = this.options.locale?.code;
        if (!code) {
            return "month-first";
        }
        return DateLib.yearFirstLocales.has(code) ? "year-first" : "month-first";
    }
    /**
     * Formats the month/year pair respecting locale conventions.
     *
     * @since 9.11.0
     */
    formatMonthYear(date) {
        const { locale, timeZone, numerals } = this.options;
        const localeCode = locale?.code;
        if (localeCode && DateLib.yearFirstLocales.has(localeCode)) {
            try {
                const intl = new Intl.DateTimeFormat(localeCode, {
                    month: "long",
                    year: "numeric",
                    timeZone,
                    numberingSystem: numerals,
                });
                const formatted = intl.format(date);
                return formatted;
            }
            catch {
                // Fallback to date-fns formatting below.
            }
        }
        const pattern = this.getMonthYearOrder() === "year-first" ? "y LLLL" : "LLLL y";
        return this.format(date, pattern);
    }
}
DateLib.yearFirstLocales = new Set([
    "eu",
    "hu",
    "ja",
    "ja-Hira",
    "ja-JP",
    "ko",
    "ko-KR",
    "lt",
    "lt-LT",
    "lv",
    "lv-LV",
    "mn",
    "mn-MN",
    "zh",
    "zh-CN",
    "zh-HK",
    "zh-TW",
]);
/**
 * The default date library with English locale.
 *
 * @since 9.2.0
 */
const defaultDateLib = new DateLib();

/**
 * Represents a day displayed in the calendar.
 *
 * In DayPicker, a `CalendarDay` is a wrapper around a `Date` object that
 * provides additional information about the day, such as whether it belongs to
 * the displayed month.
 */
class CalendarDay {
    constructor(date, displayMonth, dateLib = defaultDateLib) {
        this.date = date;
        this.displayMonth = displayMonth;
        this.outside = Boolean(displayMonth && !dateLib.isSameMonth(date, displayMonth));
        this.dateLib = dateLib;
        this.isoDate = dateLib.format(date, "yyyy-MM-dd");
        this.displayMonthId = dateLib.format(displayMonth, "yyyy-MM");
        this.dateMonthId = dateLib.format(date, "yyyy-MM");
    }
    /**
     * Checks if this day is equal to another `CalendarDay`, considering both the
     * date and the displayed month.
     *
     * @param day The `CalendarDay` to compare with.
     * @returns `true` if the days are equal, otherwise `false`.
     */
    isEqualTo(day) {
        return (this.dateLib.isSameDay(day.date, this.date) &&
            this.dateLib.isSameMonth(day.displayMonth, this.displayMonth));
    }
}

/**
 * Represents a month in a calendar year.
 *
 * A `CalendarMonth` contains the weeks within the month and the date of the
 * month.
 */
class CalendarMonth {
    constructor(month, weeks) {
        this.date = month;
        this.weeks = weeks;
    }
}

/**
 * Represents a week in a calendar month.
 *
 * A `CalendarWeek` contains the days within the week and the week number.
 */
class CalendarWeek {
    constructor(weekNumber, days) {
        this.days = days;
        this.weekNumber = weekNumber;
    }
}

/**
 * Render the button elements in the calendar.
 *
 * @private
 * @deprecated Use `PreviousMonthButton` or `@link NextMonthButton` instead.
 */
function Button(props) {
    return React3.createElement("button", { ...props });
}

/**
 * Render the label in the month caption.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function CaptionLabel(props) {
    return React3.createElement("span", { ...props });
}

/**
 * Render the chevron icon used in the navigation buttons and dropdowns.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Chevron(props) {
    const { size = 24, orientation = "left", className } = props;
    return (
    // biome-ignore lint/a11y/noSvgWithoutTitle: handled by the parent component
    React3.createElement("svg", { className: className, width: size, height: size, viewBox: "0 0 24 24" },
        orientation === "up" && (React3.createElement("polygon", { points: "6.77 17 12.5 11.43 18.24 17 20 15.28 12.5 8 5 15.28" })),
        orientation === "down" && (React3.createElement("polygon", { points: "6.77 8 12.5 13.57 18.24 8 20 9.72 12.5 17 5 9.72" })),
        orientation === "left" && (React3.createElement("polygon", { points: "16 18.112 9.81111111 12 16 5.87733333 14.0888889 4 6 12 14.0888889 20" })),
        orientation === "right" && (React3.createElement("polygon", { points: "8 18.112 14.18888889 12 8 5.87733333 9.91111111 4 18 12 9.91111111 20" }))));
}

/**
 * Render a grid cell for a specific day in the calendar.
 *
 * Handles interaction and focus for the day. If you only need to change the
 * content of the day cell, consider swapping the `DayButton` component
 * instead.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Day(props) {
    const { day, modifiers, ...tdProps } = props;
    return React3.createElement("td", { ...tdProps });
}

/**
 * Render a button for a specific day in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function DayButton(props) {
    const { day, modifiers, ...buttonProps } = props;
    const ref = React3.useRef(null);
    React3.useEffect(() => {
        if (modifiers.focused)
            ref.current?.focus();
    }, [modifiers.focused]);
    return React3.createElement("button", { ref: ref, ...buttonProps });
}

/**
 * Enum representing the UI elements composing DayPicker. These elements are
 * mapped to {@link CustomComponents}, {@link ClassNames}, and {@link Styles}.
 *
 * Some elements are extended by flags and modifiers.
 */
var UI;
(function (UI) {
    /** The root component displaying the months and the navigation bar. */
    UI["Root"] = "root";
    /** The Chevron SVG element used by navigation buttons and dropdowns. */
    UI["Chevron"] = "chevron";
    /**
     * The grid cell with the day's date. Extended by {@link DayFlag} and
     * {@link SelectionState}.
     */
    UI["Day"] = "day";
    /** The button containing the formatted day's date, inside the grid cell. */
    UI["DayButton"] = "day_button";
    /** The caption label of the month (when not showing the dropdown navigation). */
    UI["CaptionLabel"] = "caption_label";
    /** The container of the dropdown navigation (when enabled). */
    UI["Dropdowns"] = "dropdowns";
    /** The dropdown element to select for years and months. */
    UI["Dropdown"] = "dropdown";
    /** The container element of the dropdown. */
    UI["DropdownRoot"] = "dropdown_root";
    /** The root element of the footer. */
    UI["Footer"] = "footer";
    /** The month grid. */
    UI["MonthGrid"] = "month_grid";
    /** Contains the dropdown navigation or the caption label. */
    UI["MonthCaption"] = "month_caption";
    /** The dropdown with the months. */
    UI["MonthsDropdown"] = "months_dropdown";
    /** Wrapper of the month grid. */
    UI["Month"] = "month";
    /** The container of the displayed months. */
    UI["Months"] = "months";
    /** The navigation bar with the previous and next buttons. */
    UI["Nav"] = "nav";
    /**
     * The next month button in the navigation. *
     *
     * @since 9.1.0
     */
    UI["NextMonthButton"] = "button_next";
    /**
     * The previous month button in the navigation.
     *
     * @since 9.1.0
     */
    UI["PreviousMonthButton"] = "button_previous";
    /** The row containing the week. */
    UI["Week"] = "week";
    /** The group of row weeks in a month (`tbody`). */
    UI["Weeks"] = "weeks";
    /** The column header with the weekday. */
    UI["Weekday"] = "weekday";
    /** The row grouping the weekdays in the column headers. */
    UI["Weekdays"] = "weekdays";
    /** The cell containing the week number. */
    UI["WeekNumber"] = "week_number";
    /** The cell header of the week numbers column. */
    UI["WeekNumberHeader"] = "week_number_header";
    /** The dropdown with the years. */
    UI["YearsDropdown"] = "years_dropdown";
})(UI || (UI = {}));
/** Enum representing flags for the {@link UI | UI.Day} element. */
var DayFlag;
(function (DayFlag) {
    /** The day is disabled. */
    DayFlag["disabled"] = "disabled";
    /** The day is hidden. */
    DayFlag["hidden"] = "hidden";
    /** The day is outside the current month. */
    DayFlag["outside"] = "outside";
    /** The day is focused. */
    DayFlag["focused"] = "focused";
    /** The day is today. */
    DayFlag["today"] = "today";
})(DayFlag || (DayFlag = {}));
/**
 * Enum representing selection states that can be applied to the
 * {@link UI | UI.Day} element in selection mode.
 */
var SelectionState;
(function (SelectionState) {
    /** The day is at the end of a selected range. */
    SelectionState["range_end"] = "range_end";
    /** The day is at the middle of a selected range. */
    SelectionState["range_middle"] = "range_middle";
    /** The day is at the start of a selected range. */
    SelectionState["range_start"] = "range_start";
    /** The day is selected. */
    SelectionState["selected"] = "selected";
})(SelectionState || (SelectionState = {}));
/**
 * Enum representing different animation states for transitioning between
 * months.
 */
var Animation;
(function (Animation) {
    /** The entering weeks when they appear before the exiting month. */
    Animation["weeks_before_enter"] = "weeks_before_enter";
    /** The exiting weeks when they disappear before the entering month. */
    Animation["weeks_before_exit"] = "weeks_before_exit";
    /** The entering weeks when they appear after the exiting month. */
    Animation["weeks_after_enter"] = "weeks_after_enter";
    /** The exiting weeks when they disappear after the entering month. */
    Animation["weeks_after_exit"] = "weeks_after_exit";
    /** The entering caption when it appears after the exiting month. */
    Animation["caption_after_enter"] = "caption_after_enter";
    /** The exiting caption when it disappears after the entering month. */
    Animation["caption_after_exit"] = "caption_after_exit";
    /** The entering caption when it appears before the exiting month. */
    Animation["caption_before_enter"] = "caption_before_enter";
    /** The exiting caption when it disappears before the entering month. */
    Animation["caption_before_exit"] = "caption_before_exit";
})(Animation || (Animation = {}));

/**
 * Render a dropdown component for navigation in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Dropdown(props) {
    const { options, className, components, classNames, ...selectProps } = props;
    const cssClassSelect = [classNames[UI.Dropdown], className].join(" ");
    const selectedOption = options?.find(({ value }) => value === selectProps.value);
    return (React3.createElement("span", { "data-disabled": selectProps.disabled, className: classNames[UI.DropdownRoot] },
        React3.createElement(components.Select, { className: cssClassSelect, ...selectProps }, options?.map(({ value, label, disabled }) => (React3.createElement(components.Option, { key: value, value: value, disabled: disabled }, label)))),
        React3.createElement("span", { className: classNames[UI.CaptionLabel], "aria-hidden": true },
            selectedOption?.label,
            React3.createElement(components.Chevron, { orientation: "down", size: 18, className: classNames[UI.Chevron] }))));
}

/**
 * Render the navigation dropdowns for the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function DropdownNav(props) {
    return React3.createElement("div", { ...props });
}

/**
 * Render the footer of the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Footer(props) {
    return React3.createElement("div", { ...props });
}

/**
 * Render the grid with the weekday header row and the weeks for a specific
 * month.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Month(props) {
    const { calendarMonth, displayIndex, ...divProps } = props;
    return React3.createElement("div", { ...divProps }, props.children);
}

/**
 * Render the caption for a month in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function MonthCaption(props) {
    const { calendarMonth, displayIndex, ...divProps } = props;
    return React3.createElement("div", { ...divProps });
}

/**
 * Render the grid of days for a specific month.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function MonthGrid(props) {
    return React3.createElement("table", { ...props });
}

/**
 * Render a container wrapping the month grids.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Months(props) {
    return React3.createElement("div", { ...props });
}

/** @ignore */
const dayPickerContext = __mf_13(undefined);
/**
 * Provides access to the DayPicker context, which includes properties and
 * methods to interact with the DayPicker component. This hook must be used
 * within a custom component.
 *
 * @template T - Use this type to refine the returned context type with a
 *   specific selection mode.
 * @returns The context to work with DayPicker.
 * @throws {Error} If the hook is used outside of a DayPicker provider.
 * @group Hooks
 * @see https://daypicker.dev/guides/custom-components
 */
function useDayPicker() {
    const context = __mf_25(dayPickerContext);
    if (context === undefined) {
        throw new Error("useDayPicker() must be used within a custom component.");
    }
    return context;
}

/**
 * Render a dropdown to navigate between months in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function MonthsDropdown(props) {
    const { components } = useDayPicker();
    return React3.createElement(components.Dropdown, { ...props });
}

/**
 * Render the navigation toolbar with buttons to navigate between months.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Nav(props) {
    const { onPreviousClick, onNextClick, previousMonth, nextMonth, ...navProps } = props;
    const { components, classNames, labels: { labelPrevious, labelNext }, } = useDayPicker();
    const handleNextClick = __mf_24((e) => {
        if (nextMonth) {
            onNextClick?.(e);
        }
    }, [nextMonth, onNextClick]);
    const handlePreviousClick = __mf_24((e) => {
        if (previousMonth) {
            onPreviousClick?.(e);
        }
    }, [previousMonth, onPreviousClick]);
    return (React3.createElement("nav", { ...navProps },
        React3.createElement(components.PreviousMonthButton, { type: "button", className: classNames[UI.PreviousMonthButton], tabIndex: previousMonth ? undefined : -1, "aria-disabled": previousMonth ? undefined : true, "aria-label": labelPrevious(previousMonth), onClick: handlePreviousClick },
            React3.createElement(components.Chevron, { disabled: previousMonth ? undefined : true, className: classNames[UI.Chevron], orientation: "left" })),
        React3.createElement(components.NextMonthButton, { type: "button", className: classNames[UI.NextMonthButton], tabIndex: nextMonth ? undefined : -1, "aria-disabled": nextMonth ? undefined : true, "aria-label": labelNext(nextMonth), onClick: handleNextClick },
            React3.createElement(components.Chevron, { disabled: nextMonth ? undefined : true, orientation: "right", className: classNames[UI.Chevron] }))));
}

/**
 * Render the button to navigate to the next month in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function NextMonthButton(props) {
    const { components } = useDayPicker();
    return React3.createElement(components.Button, { ...props });
}

/**
 * Render an `option` element.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Option(props) {
    return React3.createElement("option", { ...props });
}

/**
 * Render the button to navigate to the previous month in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function PreviousMonthButton(props) {
    const { components } = useDayPicker();
    return React3.createElement(components.Button, { ...props });
}

/**
 * Render the root element of the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Root(props) {
    const { rootRef, ...rest } = props;
    return React3.createElement("div", { ...rest, ref: rootRef });
}

/**
 * Render a `select` element.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Select(props) {
    return React3.createElement("select", { ...props });
}

/**
 * Render a table row representing a week in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Week(props) {
    const { week, ...trProps } = props;
    return React3.createElement("tr", { ...trProps });
}

/**
 * Render a table header cell with the name of a weekday (e.g., "Mo", "Tu").
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Weekday(props) {
    return React3.createElement("th", { ...props });
}

/**
 * Render the table row containing the weekday names.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Weekdays(props) {
    return (React3.createElement("thead", { "aria-hidden": true },
        React3.createElement("tr", { ...props })));
}

/**
 * Render a table cell displaying the number of the week.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function WeekNumber(props) {
    const { week, ...thProps } = props;
    return React3.createElement("th", { ...thProps });
}

/**
 * Render the header cell for the week numbers column.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function WeekNumberHeader(props) {
    return React3.createElement("th", { ...props });
}

/**
 * Render the container for the weeks in the month grid.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function Weeks(props) {
    return React3.createElement("tbody", { ...props });
}

/**
 * Render a dropdown to navigate between years in the calendar.
 *
 * @group Components
 * @see https://daypicker.dev/guides/custom-components
 */
function YearsDropdown(props) {
    const { components } = useDayPicker();
    return React3.createElement(components.Dropdown, { ...props });
}

const components = /*#__PURE__*/Object.freeze(/*#__PURE__*/Object.defineProperty({
  __proto__: null,
  Button,
  CaptionLabel,
  Chevron,
  Day,
  DayButton,
  Dropdown,
  DropdownNav,
  Footer,
  Month,
  MonthCaption,
  MonthGrid,
  Months,
  MonthsDropdown,
  Nav,
  NextMonthButton,
  Option,
  PreviousMonthButton,
  Root,
  Select,
  Week,
  WeekNumber,
  WeekNumberHeader,
  Weekday,
  Weekdays,
  Weeks,
  YearsDropdown
}, Symbol.toStringTag, { value: 'Module' }));

/**
 * Checks if a given date is within a specified date range.
 *
 * @since 9.0.0
 * @param range - The date range to check against.
 * @param date - The date to check.
 * @param excludeEnds - If `true`, the range's start and end dates are excluded.
 * @param dateLib - The date utility library instance.
 * @returns `true` if the date is within the range, otherwise `false`.
 * @group Utilities
 */
function rangeIncludesDate(range, date, excludeEnds = false, dateLib = defaultDateLib) {
    let { from, to } = range;
    const { differenceInCalendarDays, isSameDay } = dateLib;
    if (from && to) {
        const isRangeInverted = differenceInCalendarDays(to, from) < 0;
        if (isRangeInverted) {
            [from, to] = [to, from];
        }
        const isInRange = differenceInCalendarDays(date, from) >= (excludeEnds ? 1 : 0) &&
            differenceInCalendarDays(to, date) >= (excludeEnds ? 1 : 0);
        return isInRange;
    }
    if (!excludeEnds && to) {
        return isSameDay(to, date);
    }
    if (!excludeEnds && from) {
        return isSameDay(from, date);
    }
    return false;
}

/**
 * Checks if the given value is of type {@link DateInterval}.
 *
 * @param matcher - The value to check.
 * @returns `true` if the value is a {@link DateInterval}, otherwise `false`.
 * @group Utilities
 */
function isDateInterval(matcher) {
    return Boolean(matcher &&
        typeof matcher === "object" &&
        "before" in matcher &&
        "after" in matcher);
}
/**
 * Checks if the given value is of type {@link DateRange}.
 *
 * @param value - The value to check.
 * @returns `true` if the value is a {@link DateRange}, otherwise `false`.
 * @group Utilities
 */
function isDateRange(value) {
    return Boolean(value && typeof value === "object" && "from" in value);
}
/**
 * Checks if the given value is of type {@link DateAfter}.
 *
 * @param value - The value to check.
 * @returns `true` if the value is a {@link DateAfter}, otherwise `false`.
 * @group Utilities
 */
function isDateAfterType(value) {
    return Boolean(value && typeof value === "object" && "after" in value);
}
/**
 * Checks if the given value is of type {@link DateBefore}.
 *
 * @param value - The value to check.
 * @returns `true` if the value is a {@link DateBefore}, otherwise `false`.
 * @group Utilities
 */
function isDateBeforeType(value) {
    return Boolean(value && typeof value === "object" && "before" in value);
}
/**
 * Checks if the given value is of type {@link DayOfWeek}.
 *
 * @param value - The value to check.
 * @returns `true` if the value is a {@link DayOfWeek}, otherwise `false`.
 * @group Utilities
 */
function isDayOfWeekType(value) {
    return Boolean(value && typeof value === "object" && "dayOfWeek" in value);
}
/**
 * Checks if the given value is an array of valid dates.
 *
 * @private
 * @param value - The value to check.
 * @param dateLib - The date utility library instance.
 * @returns `true` if the value is an array of valid dates, otherwise `false`.
 */
function isDatesArray(value, dateLib) {
    return Array.isArray(value) && value.every(dateLib.isDate);
}

/**
 * Checks if a given date matches at least one of the specified {@link Matcher}.
 *
 * @param date - The date to check.
 * @param matchers - The matchers to check against.
 * @param dateLib - The date utility library instance.
 * @returns `true` if the date matches any of the matchers, otherwise `false`.
 * @group Utilities
 */
function dateMatchModifiers(date, matchers, dateLib = defaultDateLib) {
    const matchersArr = !Array.isArray(matchers) ? [matchers] : matchers;
    const { isSameDay, differenceInCalendarDays, isAfter } = dateLib;
    return matchersArr.some((matcher) => {
        if (typeof matcher === "boolean") {
            return matcher;
        }
        if (dateLib.isDate(matcher)) {
            return isSameDay(date, matcher);
        }
        if (isDatesArray(matcher, dateLib)) {
            return matcher.some((matcherDate) => isSameDay(date, matcherDate));
        }
        if (isDateRange(matcher)) {
            return rangeIncludesDate(matcher, date, false, dateLib);
        }
        if (isDayOfWeekType(matcher)) {
            if (!Array.isArray(matcher.dayOfWeek)) {
                return matcher.dayOfWeek === date.getDay();
            }
            return matcher.dayOfWeek.includes(date.getDay());
        }
        if (isDateInterval(matcher)) {
            const diffBefore = differenceInCalendarDays(matcher.before, date);
            const diffAfter = differenceInCalendarDays(matcher.after, date);
            const isDayBefore = diffBefore > 0;
            const isDayAfter = diffAfter < 0;
            const isClosedInterval = isAfter(matcher.before, matcher.after);
            if (isClosedInterval) {
                return isDayAfter && isDayBefore;
            }
            else {
                return isDayBefore || isDayAfter;
            }
        }
        if (isDateAfterType(matcher)) {
            return differenceInCalendarDays(date, matcher.after) > 0;
        }
        if (isDateBeforeType(matcher)) {
            return differenceInCalendarDays(matcher.before, date) > 0;
        }
        if (typeof matcher === "function") {
            return matcher(date);
        }
        return false;
    });
}

/**
 * Creates a function to retrieve the modifiers for a given day.
 *
 * This function calculates both internal and custom modifiers for each day
 * based on the provided calendar days and DayPicker props.
 *
 * @private
 * @param days The array of `CalendarDay` objects to process.
 * @param props The DayPicker props, including modifiers and configuration
 *   options.
 * @param dateLib The date library to use for date manipulation.
 * @returns A function that retrieves the modifiers for a given `CalendarDay`.
 */
function createGetModifiers(days, props, navStart, navEnd, dateLib) {
    const { disabled, hidden, modifiers, showOutsideDays, broadcastCalendar, today = dateLib.today(), } = props;
    const { isSameDay, isSameMonth, startOfMonth, isBefore, endOfMonth, isAfter, } = dateLib;
    const computedNavStart = navStart && startOfMonth(navStart);
    const computedNavEnd = navEnd && endOfMonth(navEnd);
    const internalModifiersMap = {
        [DayFlag.focused]: [],
        [DayFlag.outside]: [],
        [DayFlag.disabled]: [],
        [DayFlag.hidden]: [],
        [DayFlag.today]: [],
    };
    const customModifiersMap = {};
    for (const day of days) {
        const { date, displayMonth } = day;
        const isOutside = Boolean(displayMonth && !isSameMonth(date, displayMonth));
        const isBeforeNavStart = Boolean(computedNavStart && isBefore(date, computedNavStart));
        const isAfterNavEnd = Boolean(computedNavEnd && isAfter(date, computedNavEnd));
        const isDisabled = Boolean(disabled && dateMatchModifiers(date, disabled, dateLib));
        const isHidden = Boolean(hidden && dateMatchModifiers(date, hidden, dateLib)) ||
            isBeforeNavStart ||
            isAfterNavEnd ||
            // Broadcast calendar will show outside days as default
            (!broadcastCalendar && !showOutsideDays && isOutside) ||
            (broadcastCalendar && showOutsideDays === false && isOutside);
        const isToday = isSameDay(date, today);
        if (isOutside)
            internalModifiersMap.outside.push(day);
        if (isDisabled)
            internalModifiersMap.disabled.push(day);
        if (isHidden)
            internalModifiersMap.hidden.push(day);
        if (isToday)
            internalModifiersMap.today.push(day);
        // Add custom modifiers
        if (modifiers) {
            Object.keys(modifiers).forEach((name) => {
                const modifierValue = modifiers?.[name];
                const isMatch = modifierValue
                    ? dateMatchModifiers(date, modifierValue, dateLib)
                    : false;
                if (!isMatch)
                    return;
                if (customModifiersMap[name]) {
                    customModifiersMap[name].push(day);
                }
                else {
                    customModifiersMap[name] = [day];
                }
            });
        }
    }
    return (day) => {
        // Initialize all the modifiers to false
        const dayFlags = {
            [DayFlag.focused]: false,
            [DayFlag.disabled]: false,
            [DayFlag.hidden]: false,
            [DayFlag.outside]: false,
            [DayFlag.today]: false,
        };
        const customModifiers = {};
        // Find the modifiers for the given day
        for (const name in internalModifiersMap) {
            const days = internalModifiersMap[name];
            dayFlags[name] = days.some((d) => d === day);
        }
        for (const name in customModifiersMap) {
            customModifiers[name] = customModifiersMap[name].some((d) => d === day);
        }
        return {
            ...dayFlags,
            // custom modifiers should override all the previous ones
            ...customModifiers,
        };
    };
}

/**
 * Returns the class names for a day based on its modifiers.
 *
 * This function combines the base class name for the day with any class names
 * associated with active modifiers.
 *
 * @param modifiers The modifiers applied to the day.
 * @param classNames The base class names for the calendar elements.
 * @param modifiersClassNames The class names associated with specific
 *   modifiers.
 * @returns An array of class names for the day.
 */
function getClassNamesForModifiers(modifiers, classNames, modifiersClassNames = {}) {
    const modifierClassNames = Object.entries(modifiers)
        .filter(([, active]) => active === true)
        .reduce((previousValue, [key]) => {
        if (modifiersClassNames[key]) {
            previousValue.push(modifiersClassNames[key]);
        }
        else if (classNames[DayFlag[key]]) {
            previousValue.push(classNames[DayFlag[key]]);
        }
        else if (classNames[SelectionState[key]]) {
            previousValue.push(classNames[SelectionState[key]]);
        }
        return previousValue;
    }, [classNames[UI.Day]]);
    return modifierClassNames;
}

/**
 * Merges custom components from the props with the default components.
 *
 * This function ensures that any custom components provided in the props
 * override the default components.
 *
 * @param customComponents The custom components provided in the DayPicker
 *   props.
 * @returns An object containing the merged components.
 */
function getComponents(customComponents) {
    return {
        ...components,
        ...customComponents,
    };
}

/**
 * Extracts `data-` attributes from the DayPicker props.
 *
 * This function collects all `data-` attributes from the props and adds
 * additional attributes based on the DayPicker configuration.
 *
 * @param props The DayPicker props.
 * @returns An object containing the `data-` attributes.
 */
function getDataAttributes(props) {
    const dataAttributes = {
        "data-mode": props.mode ?? undefined,
        "data-required": "required" in props ? props.required : undefined,
        "data-multiple-months": (props.numberOfMonths && props.numberOfMonths > 1) || undefined,
        "data-week-numbers": props.showWeekNumber || undefined,
        "data-broadcast-calendar": props.broadcastCalendar || undefined,
        "data-nav-layout": props.navLayout || undefined,
    };
    Object.entries(props).forEach(([key, val]) => {
        if (key.startsWith("data-")) {
            dataAttributes[key] = val;
        }
    });
    return dataAttributes;
}

/**
 * Returns the default class names for the UI elements.
 *
 * This function generates a mapping of default class names for various UI
 * elements, day flags, selection states, and animations.
 *
 * @returns An object containing the default class names.
 * @group Utilities
 */
function getDefaultClassNames() {
    const classNames = {};
    for (const key in UI) {
        classNames[UI[key]] =
            `rdp-${UI[key]}`;
    }
    for (const key in DayFlag) {
        classNames[DayFlag[key]] =
            `rdp-${DayFlag[key]}`;
    }
    for (const key in SelectionState) {
        classNames[SelectionState[key]] =
            `rdp-${SelectionState[key]}`;
    }
    for (const key in Animation) {
        classNames[Animation[key]] =
            `rdp-${Animation[key]}`;
    }
    return classNames;
}

/**
 * Formats the caption of the month.
 *
 * @defaultValue Locale-specific month/year order (e.g., "November 2022").
 * @param month The date representing the month.
 * @param options Configuration options for the date library.
 * @param dateLib The date library to use for formatting. If not provided, a new
 *   instance is created.
 * @returns The formatted caption as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatCaption(month, options, dateLib) {
    const lib = dateLib ?? new DateLib(options);
    return lib.formatMonthYear(month);
}
/**
 * @private
 * @deprecated Use {@link formatCaption} instead.
 * @group Formatters
 */
const formatMonthCaption = formatCaption;

/**
 * Formats the day date shown in the day cell.
 *
 * @defaultValue `d` (e.g., "1").
 * @param date The date to format.
 * @param options Configuration options for the date library.
 * @param dateLib The date library to use for formatting. If not provided, a new
 *   instance is created.
 * @returns The formatted day as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatDay(date, options, dateLib) {
    return (dateLib ?? new DateLib(options)).format(date, "d");
}

/**
 * Formats the month for the dropdown option label.
 *
 * @defaultValue The localized full month name.
 * @param month The date representing the month.
 * @param dateLib The date library to use for formatting. Defaults to
 *   `defaultDateLib`.
 * @returns The formatted month name as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatMonthDropdown(month, dateLib = defaultDateLib) {
    return dateLib.format(month, "LLLL");
}

/**
 * Formats the name of a weekday to be displayed in the weekdays header.
 *
 * @defaultValue `cccccc` (e.g., "Mo" for Monday).
 * @param weekday The date representing the weekday.
 * @param options Configuration options for the date library.
 * @param dateLib The date library to use for formatting. If not provided, a new
 *   instance is created.
 * @returns The formatted weekday name as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatWeekdayName(weekday, options, dateLib) {
    return (dateLib ?? new DateLib(options)).format(weekday, "cccccc");
}

/**
 * Formats the week number.
 *
 * @defaultValue The week number as a string, with a leading zero for single-digit numbers.
 * @param weekNumber The week number to format.
 * @param dateLib The date library to use for formatting. Defaults to
 *   `defaultDateLib`.
 * @returns The formatted week number as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatWeekNumber(weekNumber, dateLib = defaultDateLib) {
    if (weekNumber < 10) {
        return dateLib.formatNumber(`0${weekNumber.toLocaleString()}`);
    }
    return dateLib.formatNumber(`${weekNumber.toLocaleString()}`);
}

/**
 * Formats the header for the week number column.
 *
 * @defaultValue An empty string `""`.
 * @returns The formatted week number header as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatWeekNumberHeader() {
    return ``;
}

/**
 * Formats the year for the dropdown option label.
 *
 * @param year The year to format.
 * @param dateLib The date library to use for formatting. Defaults to
 *   `defaultDateLib`.
 * @returns The formatted year as a string.
 * @group Formatters
 * @see https://daypicker.dev/docs/translation#custom-formatters
 */
function formatYearDropdown(year, dateLib = defaultDateLib) {
    return dateLib.format(year, "yyyy");
}
/**
 * @private
 * @deprecated Use `formatYearDropdown` instead.
 * @group Formatters
 */
const formatYearCaption = formatYearDropdown;

const defaultFormatters = /*#__PURE__*/Object.freeze(/*#__PURE__*/Object.defineProperty({
  __proto__: null,
  formatCaption,
  formatDay,
  formatMonthCaption,
  formatMonthDropdown,
  formatWeekNumber,
  formatWeekNumberHeader,
  formatWeekdayName,
  formatYearCaption,
  formatYearDropdown
}, Symbol.toStringTag, { value: 'Module' }));

/**
 * Merges custom formatters from the props with the default formatters.
 *
 * @param customFormatters The custom formatters provided in the DayPicker
 *   props.
 * @returns The merged formatters object.
 */
function getFormatters(customFormatters) {
    if (customFormatters?.formatMonthCaption && !customFormatters.formatCaption) {
        customFormatters.formatCaption = customFormatters.formatMonthCaption;
    }
    if (customFormatters?.formatYearCaption &&
        !customFormatters.formatYearDropdown) {
        customFormatters.formatYearDropdown = customFormatters.formatYearCaption;
    }
    return {
        ...defaultFormatters,
        ...customFormatters,
    };
}

/**
 * Generates the ARIA label for a day button.
 *
 * Use the `modifiers` argument to provide additional context for the label,
 * such as indicating if the day is "today" or "selected."
 *
 * @defaultValue The formatted date.
 * @param date - The date to format.
 * @param modifiers - The modifiers providing context for the day.
 * @param options - Optional configuration for the date formatting library.
 * @param dateLib - An optional instance of the date formatting library.
 * @returns The ARIA label for the day button.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelDayButton(date, modifiers, options, dateLib) {
    let label = (dateLib ?? new DateLib(options)).format(date, "PPPP");
    if (modifiers.today)
        label = `Today, ${label}`;
    if (modifiers.selected)
        label = `${label}, selected`;
    return label;
}
/**
 * @ignore
 * @deprecated Use `labelDayButton` instead.
 */
const labelDay = labelDayButton;

/**
 * Generates the ARIA label for the month grid, which is announced when entering
 * the grid.
 *
 * @defaultValue Locale-specific month/year order (e.g., "November 2022").
 * @param date - The date representing the month.
 * @param options - Optional configuration for the date formatting library.
 * @param dateLib - An optional instance of the date formatting library.
 * @returns The ARIA label for the month grid.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelGrid(date, options, dateLib) {
    const lib = dateLib ?? new DateLib(options);
    return lib.formatMonthYear(date);
}
/**
 * @ignore
 * @deprecated Use {@link labelGrid} instead.
 */
const labelCaption = labelGrid;

/**
 * Generates the label for a day grid cell when the calendar is not interactive.
 *
 * @param date - The date to format.
 * @param modifiers - Optional modifiers providing context for the day.
 * @param options - Optional configuration for the date formatting library.
 * @param dateLib - An optional instance of the date formatting library.
 * @returns The label for the day grid cell.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelGridcell(date, modifiers, options, dateLib) {
    let label = (dateLib ?? new DateLib(options)).format(date, "PPPP");
    if (modifiers?.today) {
        label = `Today, ${label}`;
    }
    return label;
}

/**
 * Generates the ARIA label for the months dropdown.
 *
 * @defaultValue `"Choose the Month"`
 * @param options - Optional configuration for the date formatting library.
 * @returns The ARIA label for the months dropdown.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelMonthDropdown(_options) {
    return "Choose the Month";
}

/**
 * Generates the ARIA label for the navigation toolbar.
 *
 * @defaultValue `""`
 * @returns The ARIA label for the navigation toolbar.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelNav() {
    return "";
}

const defaultLabel = "Go to the Next Month";
/**
 * Generates the ARIA label for the "next month" button.
 *
 * @defaultValue `"Go to the Next Month"`
 * @param month - The date representing the next month, or `undefined` if there
 *   is no next month.
 * @returns The ARIA label for the "next month" button.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelNext(_month, _options) {
    return defaultLabel;
}

/**
 * Generates the ARIA label for the "previous month" button.
 *
 * @defaultValue `"Go to the Previous Month"`
 * @param month - The date representing the previous month, or `undefined` if
 *   there is no previous month.
 * @returns The ARIA label for the "previous month" button.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelPrevious(_month) {
    return "Go to the Previous Month";
}

/**
 * Generates the ARIA label for a weekday column header.
 *
 * @defaultValue `"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"`
 * @param date - The date representing the weekday.
 * @param options - Optional configuration for the date formatting library.
 * @param dateLib - An optional instance of the date formatting library.
 * @returns The ARIA label for the weekday column header.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelWeekday(date, options, dateLib) {
    return (dateLib ?? new DateLib(options)).format(date, "cccc");
}

/**
 * Generates the ARIA label for the week number cell (the first cell in a row).
 *
 * @defaultValue `Week ${weekNumber}`
 * @param weekNumber - The number of the week.
 * @param options - Optional configuration for the date formatting library.
 * @returns The ARIA label for the week number cell.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelWeekNumber(weekNumber, _options) {
    return `Week ${weekNumber}`;
}

/**
 * Generates the ARIA label for the week number header element.
 *
 * @defaultValue `"Week Number"`
 * @param options - Optional configuration for the date formatting library.
 * @returns The ARIA label for the week number header.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelWeekNumberHeader(_options) {
    return "Week Number";
}

/**
 * Generates the ARIA label for the years dropdown.
 *
 * @defaultValue `"Choose the Year"`
 * @param options - Optional configuration for the date formatting library.
 * @returns The ARIA label for the years dropdown.
 * @group Labels
 * @see https://daypicker.dev/docs/translation#aria-labels
 */
function labelYearDropdown(_options) {
    return "Choose the Year";
}

const defaultLabels = /*#__PURE__*/Object.freeze(/*#__PURE__*/Object.defineProperty({
  __proto__: null,
  labelCaption,
  labelDay,
  labelDayButton,
  labelGrid,
  labelGridcell,
  labelMonthDropdown,
  labelNav,
  labelNext,
  labelPrevious,
  labelWeekNumber,
  labelWeekNumberHeader,
  labelWeekday,
  labelYearDropdown
}, Symbol.toStringTag, { value: 'Module' }));

const resolveLabel = (defaultLabel, customLabel, localeLabel) => {
    if (customLabel)
        return customLabel;
    if (localeLabel) {
        return (typeof localeLabel === "function"
            ? localeLabel
            : (..._args) => localeLabel);
    }
    return defaultLabel;
};
/**
 * Merges custom labels from the props with the default labels.
 *
 * When available, uses the locale-provided translation for `labelNext`.
 *
 * @param customLabels The custom labels provided in the DayPicker props.
 * @param options Options from the date library, used to resolve locale
 *   translations.
 * @returns The merged labels object with locale-aware defaults.
 */
function getLabels(customLabels, options) {
    const localeLabels = options.locale?.labels ?? {};
    return {
        ...defaultLabels,
        ...(customLabels ?? {}),
        labelDayButton: resolveLabel(labelDayButton, customLabels?.labelDayButton, localeLabels.labelDayButton),
        labelMonthDropdown: resolveLabel(labelMonthDropdown, customLabels?.labelMonthDropdown, localeLabels.labelMonthDropdown),
        labelNext: resolveLabel(labelNext, customLabels?.labelNext, localeLabels.labelNext),
        labelPrevious: resolveLabel(labelPrevious, customLabels?.labelPrevious, localeLabels.labelPrevious),
        labelWeekNumber: resolveLabel(labelWeekNumber, customLabels?.labelWeekNumber, localeLabels.labelWeekNumber),
        labelYearDropdown: resolveLabel(labelYearDropdown, customLabels?.labelYearDropdown, localeLabels.labelYearDropdown),
        labelGrid: resolveLabel(labelGrid, customLabels?.labelGrid, localeLabels.labelGrid),
        labelGridcell: resolveLabel(labelGridcell, customLabels?.labelGridcell, localeLabels.labelGridcell),
        labelNav: resolveLabel(labelNav, customLabels?.labelNav, localeLabels.labelNav),
        labelWeekNumberHeader: resolveLabel(labelWeekNumberHeader, customLabels?.labelWeekNumberHeader, localeLabels.labelWeekNumberHeader),
        labelWeekday: resolveLabel(labelWeekday, customLabels?.labelWeekday, localeLabels.labelWeekday),
    };
}

/**
 * Returns the months to show in the dropdown.
 *
 * This function generates a list of months for the current year, formatted
 * using the provided formatter, and determines whether each month should be
 * disabled based on the navigation range.
 *
 * @param displayMonth The currently displayed month.
 * @param navStart The start date for navigation.
 * @param navEnd The end date for navigation.
 * @param formatters The formatters to use for formatting the month labels.
 * @param dateLib The date library to use for date manipulation.
 * @returns An array of dropdown options representing the months, or `undefined`
 *   if no months are available.
 */
function getMonthOptions(displayMonth, navStart, navEnd, formatters, dateLib) {
    const { startOfMonth, startOfYear, endOfYear, eachMonthOfInterval, getMonth, } = dateLib;
    const months = eachMonthOfInterval({
        start: startOfYear(displayMonth),
        end: endOfYear(displayMonth),
    });
    const options = months.map((month) => {
        const label = formatters.formatMonthDropdown(month, dateLib);
        const value = getMonth(month);
        const disabled = (navStart && month < startOfMonth(navStart)) ||
            (navEnd && month > startOfMonth(navEnd)) ||
            false;
        return { value, label, disabled };
    });
    return options;
}

/**
 * Returns the computed style for a day based on its modifiers.
 *
 * This function merges the base styles for the day with any styles associated
 * with active modifiers.
 *
 * @param dayModifiers The modifiers applied to the day.
 * @param styles The base styles for the calendar elements.
 * @param modifiersStyles The styles associated with specific modifiers.
 * @returns The computed style for the day.
 */
function getStyleForModifiers(dayModifiers, styles = {}, modifiersStyles = {}) {
    let style = { ...styles?.[UI.Day] };
    Object.entries(dayModifiers)
        .filter(([, active]) => active === true)
        .forEach(([modifier]) => {
        style = {
            ...style,
            ...modifiersStyles?.[modifier],
        };
    });
    return style;
}

/**
 * Generates a series of 7 days, starting from the beginning of the week, to use
 * for formatting weekday names (e.g., Monday, Tuesday, etc.).
 *
 * @param dateLib The date library to use for date manipulation.
 * @param ISOWeek Whether to use ISO week numbering (weeks start on Monday).
 * @param broadcastCalendar Whether to use the broadcast calendar (weeks start
 *   on Monday, but may include adjustments for broadcast-specific rules).
 * @returns An array of 7 dates representing the weekdays.
 */
function getWeekdays(dateLib, ISOWeek, broadcastCalendar, today) {
    const referenceToday = today ?? dateLib.today();
    const start = broadcastCalendar
        ? dateLib.startOfBroadcastWeek(referenceToday, dateLib)
        : ISOWeek
            ? dateLib.startOfISOWeek(referenceToday)
            : dateLib.startOfWeek(referenceToday);
    const days = [];
    for (let i = 0; i < 7; i++) {
        const day = dateLib.addDays(start, i);
        days.push(day);
    }
    return days;
}

/**
 * Returns the years to display in the dropdown.
 *
 * This function generates a list of years between the navigation start and end
 * dates, formatted using the provided formatter.
 *
 * @param navStart The start date for navigation.
 * @param navEnd The end date for navigation.
 * @param formatters The formatters to use for formatting the year labels.
 * @param dateLib The date library to use for date manipulation.
 * @param reverse If true, reverses the order of the years (descending).
 * @returns An array of dropdown options representing the years, or `undefined`
 *   if `navStart` or `navEnd` is not provided.
 */
function getYearOptions(navStart, navEnd, formatters, dateLib, reverse = false) {
    if (!navStart)
        return undefined;
    if (!navEnd)
        return undefined;
    const { startOfYear, endOfYear, eachYearOfInterval, getYear } = dateLib;
    const firstNavYear = startOfYear(navStart);
    const lastNavYear = endOfYear(navEnd);
    const years = eachYearOfInterval({ start: firstNavYear, end: lastNavYear });
    if (reverse)
        years.reverse();
    return years.map((year) => {
        const label = formatters.formatYearDropdown(year, dateLib);
        return {
            value: getYear(year),
            label,
            disabled: false,
        };
    });
}

/**
 * Creates `dateLib` overrides that keep all calendar math at noon in the target
 * time zone. This avoids second-level offset changes (e.g., historical zones
 * with +03:41:12) from pushing dates backward across midnight.
 */
function createNoonOverrides(timeZone, options = {}) {
    const { weekStartsOn, locale } = options;
    const fallbackWeekStartsOn = (weekStartsOn ??
        locale?.options?.weekStartsOn ??
        0);
    // Keep all internal math anchored at noon in the target zone to avoid
    // historical second-level offsets from crossing midnight.
    const toNoonTZDate = (date) => {
        const normalizedDate = typeof date === "number" || typeof date === "string"
            ? new Date(date)
            : date;
        return new TZDate(normalizedDate.getFullYear(), normalizedDate.getMonth(), normalizedDate.getDate(), 12, 0, 0, timeZone);
    };
    // Convert a value into a host `Date` that represents the same calendar day
    // as the target-zone noon. This is useful for helpers (e.g., date-fns week
    // utilities) that expect local `Date` instances rather than `TZDate`s.
    const toCalendarDate = (date) => {
        const zoned = toNoonTZDate(date);
        return new Date(zoned.getFullYear(), zoned.getMonth(), zoned.getDate(), 0, 0, 0, 0);
    };
    return {
        today: () => {
            return toNoonTZDate(TZDate.tz(timeZone));
        },
        newDate: (year, monthIndex, date) => {
            return new TZDate(year, monthIndex, date, 12, 0, 0, timeZone);
        },
        startOfDay: (date) => {
            return toNoonTZDate(date);
        },
        startOfWeek: (date, options) => {
            const base = toNoonTZDate(date);
            const weekStartsOnValue = (options?.weekStartsOn ??
                fallbackWeekStartsOn);
            const diff = (base.getDay() - weekStartsOnValue + 7) % 7;
            base.setDate(base.getDate() - diff);
            return base;
        },
        startOfISOWeek: (date) => {
            const base = toNoonTZDate(date);
            const diff = (base.getDay() - 1 + 7) % 7;
            base.setDate(base.getDate() - diff);
            return base;
        },
        startOfMonth: (date) => {
            const base = toNoonTZDate(date);
            base.setDate(1);
            return base;
        },
        startOfYear: (date) => {
            const base = toNoonTZDate(date);
            base.setMonth(0, 1);
            return base;
        },
        endOfWeek: (date, options) => {
            const base = toNoonTZDate(date);
            const weekStartsOnValue = (options?.weekStartsOn ??
                fallbackWeekStartsOn);
            const endDow = (weekStartsOnValue + 6) % 7;
            const diff = (endDow - base.getDay() + 7) % 7;
            base.setDate(base.getDate() + diff);
            return base;
        },
        endOfISOWeek: (date) => {
            const base = toNoonTZDate(date);
            const diff = (7 - base.getDay()) % 7;
            base.setDate(base.getDate() + diff);
            return base;
        },
        endOfMonth: (date) => {
            const base = toNoonTZDate(date);
            base.setMonth(base.getMonth() + 1, 0);
            return base;
        },
        endOfYear: (date) => {
            const base = toNoonTZDate(date);
            base.setMonth(11, 31);
            return base;
        },
        eachMonthOfInterval: (interval) => {
            const start = toNoonTZDate(interval.start);
            const end = toNoonTZDate(interval.end);
            const result = [];
            const cursor = new TZDate(start.getFullYear(), start.getMonth(), 1, 12, 0, 0, timeZone);
            const endKey = end.getFullYear() * 12 + end.getMonth();
            while (cursor.getFullYear() * 12 + cursor.getMonth() <= endKey) {
                result.push(new TZDate(cursor, timeZone));
                cursor.setMonth(cursor.getMonth() + 1, 1);
            }
            return result;
        },
        // Normalize to noon once before arithmetic (avoid DST/midnight edge cases),
        // mutate the same TZDate, and return it.
        addDays: (date, amount) => {
            const base = toNoonTZDate(date);
            base.setDate(base.getDate() + amount);
            return base;
        },
        addWeeks: (date, amount) => {
            const base = toNoonTZDate(date);
            base.setDate(base.getDate() + amount * 7);
            return base;
        },
        addMonths: (date, amount) => {
            const base = toNoonTZDate(date);
            base.setMonth(base.getMonth() + amount);
            return base;
        },
        addYears: (date, amount) => {
            const base = toNoonTZDate(date);
            base.setFullYear(base.getFullYear() + amount);
            return base;
        },
        eachYearOfInterval: (interval) => {
            const start = toNoonTZDate(interval.start);
            const end = toNoonTZDate(interval.end);
            const years = [];
            const cursor = new TZDate(start.getFullYear(), 0, 1, 12, 0, 0, timeZone);
            while (cursor.getFullYear() <= end.getFullYear()) {
                years.push(new TZDate(cursor, timeZone));
                cursor.setFullYear(cursor.getFullYear() + 1, 0, 1);
            }
            return years;
        },
        getWeek: (date, options) => {
            const base = toCalendarDate(date);
            return getWeek(base, {
                weekStartsOn: options?.weekStartsOn ?? fallbackWeekStartsOn,
                firstWeekContainsDate: options?.firstWeekContainsDate ??
                    locale?.options?.firstWeekContainsDate ??
                    1,
            });
        },
        getISOWeek: (date) => {
            const base = toCalendarDate(date);
            return getISOWeek(base);
        },
        differenceInCalendarDays: (dateLeft, dateRight) => {
            const left = toCalendarDate(dateLeft);
            const right = toCalendarDate(dateRight);
            return differenceInCalendarDays(left, right);
        },
        differenceInCalendarMonths: (dateLeft, dateRight) => {
            const left = toCalendarDate(dateLeft);
            const right = toCalendarDate(dateRight);
            return differenceInCalendarMonths(left, right);
        },
    };
}

const asHtmlElement = (element) => {
    if (element instanceof HTMLElement)
        return element;
    return null;
};
const queryMonthEls = (element) => [
    ...(element.querySelectorAll("[data-animated-month]") ?? []),
];
const queryMonthEl = (element) => asHtmlElement(element.querySelector("[data-animated-month]"));
const queryCaptionEl = (element) => asHtmlElement(element.querySelector("[data-animated-caption]"));
const queryWeeksEl = (element) => asHtmlElement(element.querySelector("[data-animated-weeks]"));
const queryNavEl = (element) => asHtmlElement(element.querySelector("[data-animated-nav]"));
const queryWeekdaysEl = (element) => asHtmlElement(element.querySelector("[data-animated-weekdays]"));
/**
 * Handles animations for transitioning between months in the DayPicker
 * component.
 *
 * @private
 * @param rootElRef - A reference to the root element of the DayPicker
 *   component.
 * @param enabled - Whether animations are enabled.
 * @param options - Configuration options for the animation, including class
 *   names, months, focused day, and the date utility library.
 */
function useAnimation(rootElRef, enabled, { classNames, months, focused, dateLib, }) {
    const previousRootElSnapshotRef = __mf_37(null);
    const previousMonthsRef = __mf_37(months);
    const animatingRef = __mf_37(false);
    __mf_33(() => {
        // get previous months before updating the previous months ref
        const previousMonths = previousMonthsRef.current;
        // update previous months ref for next effect trigger
        previousMonthsRef.current = months;
        if (!enabled ||
            !rootElRef.current ||
            // safety check because the ref can be set to anything by consumers
            !(rootElRef.current instanceof HTMLElement) ||
            // validation required for the animation to work as expected
            months.length === 0 ||
            previousMonths.length === 0 ||
            months.length !== previousMonths.length) {
            return;
        }
        const isSameMonth = dateLib.isSameMonth(months[0].date, previousMonths[0].date);
        const isAfterPreviousMonth = dateLib.isAfter(months[0].date, previousMonths[0].date);
        const captionAnimationClass = isAfterPreviousMonth
            ? classNames[Animation.caption_after_enter]
            : classNames[Animation.caption_before_enter];
        const weeksAnimationClass = isAfterPreviousMonth
            ? classNames[Animation.weeks_after_enter]
            : classNames[Animation.weeks_before_enter];
        // get previous root element snapshot before updating the snapshot ref
        const previousRootElSnapshot = previousRootElSnapshotRef.current;
        // update snapshot for next effect trigger
        const rootElSnapshot = rootElRef.current.cloneNode(true);
        if (rootElSnapshot instanceof HTMLElement) {
            // if this effect is triggered while animating, we need to clean up the new root snapshot
            // to put it in the same state as when not animating, to correctly animate the next month change
            const currentMonthElsSnapshot = queryMonthEls(rootElSnapshot);
            currentMonthElsSnapshot.forEach((currentMonthElSnapshot) => {
                if (!(currentMonthElSnapshot instanceof HTMLElement))
                    return;
                // remove the old month snapshots from the new root snapshot
                const previousMonthElSnapshot = queryMonthEl(currentMonthElSnapshot);
                if (previousMonthElSnapshot &&
                    currentMonthElSnapshot.contains(previousMonthElSnapshot)) {
                    currentMonthElSnapshot.removeChild(previousMonthElSnapshot);
                }
                // remove animation classes from the new month snapshots
                const captionEl = queryCaptionEl(currentMonthElSnapshot);
                if (captionEl) {
                    captionEl.classList.remove(captionAnimationClass);
                }
                const weeksEl = queryWeeksEl(currentMonthElSnapshot);
                if (weeksEl) {
                    weeksEl.classList.remove(weeksAnimationClass);
                }
            });
            previousRootElSnapshotRef.current = rootElSnapshot;
        }
        else {
            previousRootElSnapshotRef.current = null;
        }
        if (animatingRef.current ||
            isSameMonth ||
            // skip animation if a day is focused because it can cause issues to the animation and is better for a11y
            focused) {
            return;
        }
        const previousMonthEls = previousRootElSnapshot instanceof HTMLElement
            ? queryMonthEls(previousRootElSnapshot)
            : [];
        const currentMonthEls = queryMonthEls(rootElRef.current);
        if (currentMonthEls?.every((el) => el instanceof HTMLElement) &&
            previousMonthEls &&
            previousMonthEls.every((el) => el instanceof HTMLElement)) {
            animatingRef.current = true;
            // set isolation to isolate to isolate the stacking context during animation
            rootElRef.current.style.isolation = "isolate";
            // set z-index to 1 to ensure the nav is clickable over the other elements being animated
            const navEl = queryNavEl(rootElRef.current);
            if (navEl) {
                navEl.style.zIndex = "1";
            }
            currentMonthEls.forEach((currentMonthEl, index) => {
                const previousMonthEl = previousMonthEls[index];
                if (!previousMonthEl) {
                    return;
                }
                // animate new displayed month
                currentMonthEl.style.position = "relative";
                currentMonthEl.style.overflow = "hidden";
                const captionEl = queryCaptionEl(currentMonthEl);
                if (captionEl) {
                    captionEl.classList.add(captionAnimationClass);
                }
                const weeksEl = queryWeeksEl(currentMonthEl);
                if (weeksEl) {
                    weeksEl.classList.add(weeksAnimationClass);
                }
                // animate new displayed month end
                const cleanUp = () => {
                    animatingRef.current = false;
                    if (rootElRef.current) {
                        rootElRef.current.style.isolation = "";
                    }
                    if (navEl) {
                        navEl.style.zIndex = "";
                    }
                    if (captionEl) {
                        captionEl.classList.remove(captionAnimationClass);
                    }
                    if (weeksEl) {
                        weeksEl.classList.remove(weeksAnimationClass);
                    }
                    currentMonthEl.style.position = "";
                    currentMonthEl.style.overflow = "";
                    if (currentMonthEl.contains(previousMonthEl)) {
                        currentMonthEl.removeChild(previousMonthEl);
                    }
                };
                // animate old displayed month
                previousMonthEl.style.pointerEvents = "none";
                previousMonthEl.style.position = "absolute";
                previousMonthEl.style.overflow = "hidden";
                previousMonthEl.setAttribute("aria-hidden", "true");
                // hide the weekdays container of the old month and only the new one
                const previousWeekdaysEl = queryWeekdaysEl(previousMonthEl);
                if (previousWeekdaysEl) {
                    previousWeekdaysEl.style.opacity = "0";
                }
                const previousCaptionEl = queryCaptionEl(previousMonthEl);
                if (previousCaptionEl) {
                    previousCaptionEl.classList.add(isAfterPreviousMonth
                        ? classNames[Animation.caption_before_exit]
                        : classNames[Animation.caption_after_exit]);
                    previousCaptionEl.addEventListener("animationend", cleanUp);
                }
                const previousWeeksEl = queryWeeksEl(previousMonthEl);
                if (previousWeeksEl) {
                    previousWeeksEl.classList.add(isAfterPreviousMonth
                        ? classNames[Animation.weeks_before_exit]
                        : classNames[Animation.weeks_after_exit]);
                }
                currentMonthEl.insertBefore(previousMonthEl, currentMonthEl.firstChild);
            });
        }
    });
}

/**
 * Returns all the dates to display in the calendar.
 *
 * This function calculates the range of dates to display based on the provided
 * display months, constraints, and calendar configuration.
 *
 * @param displayMonths The months to display in the calendar.
 * @param maxDate The maximum date to include in the range.
 * @param props The DayPicker props, including calendar configuration options.
 * @param dateLib The date library to use for date manipulation.
 * @returns An array of dates to display in the calendar.
 */
function getDates(displayMonths, maxDate, props, dateLib) {
    const firstMonth = displayMonths[0];
    const lastMonth = displayMonths[displayMonths.length - 1];
    const { ISOWeek, fixedWeeks, broadcastCalendar } = props ?? {};
    const { addDays, differenceInCalendarDays, differenceInCalendarMonths, endOfBroadcastWeek, endOfISOWeek, endOfMonth, endOfWeek, isAfter, startOfBroadcastWeek, startOfISOWeek, startOfWeek, } = dateLib;
    const startWeekFirstDate = broadcastCalendar
        ? startOfBroadcastWeek(firstMonth, dateLib)
        : ISOWeek
            ? startOfISOWeek(firstMonth)
            : startOfWeek(firstMonth);
    const displayMonthsWeekEnd = broadcastCalendar
        ? endOfBroadcastWeek(lastMonth)
        : ISOWeek
            ? endOfISOWeek(endOfMonth(lastMonth))
            : endOfWeek(endOfMonth(lastMonth));
    // If maxDate is set, clamp the grid to the end of that week.
    const constraintWeekEnd = maxDate &&
        (broadcastCalendar
            ? endOfBroadcastWeek(maxDate)
            : ISOWeek
                ? endOfISOWeek(maxDate)
                : endOfWeek(maxDate));
    // Pick the earliest week end between the displayed months and the constraint.
    const gridEndDate = constraintWeekEnd && isAfter(displayMonthsWeekEnd, constraintWeekEnd)
        ? constraintWeekEnd
        : displayMonthsWeekEnd;
    const nOfDays = differenceInCalendarDays(gridEndDate, startWeekFirstDate);
    const nOfMonths = differenceInCalendarMonths(lastMonth, firstMonth) + 1;
    const dates = [];
    for (let i = 0; i <= nOfDays; i++) {
        const date = addDays(startWeekFirstDate, i);
        dates.push(date);
    }
    // If fixed weeks is enabled, add the extra dates to the array
    const nrOfDaysWithFixedWeeks = broadcastCalendar ? 35 : 42;
    const extraDates = nrOfDaysWithFixedWeeks * nOfMonths;
    if (fixedWeeks && dates.length < extraDates) {
        const daysToAdd = extraDates - dates.length;
        for (let i = 0; i < daysToAdd; i++) {
            const date = addDays(dates[dates.length - 1], 1);
            dates.push(date);
        }
    }
    return dates;
}

/**
 * Returns all the days belonging to the calendar by merging the days in the
 * weeks for each month.
 *
 * @param calendarMonths The array of calendar months.
 * @returns An array of `CalendarDay` objects representing all the days in the
 *   calendar.
 */
function getDays(calendarMonths) {
    const initialDays = [];
    return calendarMonths.reduce((days, month) => {
        const weekDays = month.weeks.reduce((weekDays, week) => {
            return weekDays.concat(week.days.slice());
        }, initialDays.slice());
        return days.concat(weekDays.slice());
    }, initialDays.slice());
}

/**
 * Returns the months to display in the calendar.
 *
 * @param firstDisplayedMonth The first month currently displayed in the
 *   calendar.
 * @param calendarEndMonth The latest month the user can navigate to.
 * @param props The DayPicker props, including `numberOfMonths`.
 * @param dateLib The date library to use for date manipulation.
 * @returns An array of dates representing the months to display.
 */
function getDisplayMonths(firstDisplayedMonth, calendarEndMonth, props, dateLib) {
    const { numberOfMonths = 1 } = props;
    const months = [];
    for (let i = 0; i < numberOfMonths; i++) {
        const month = dateLib.addMonths(firstDisplayedMonth, i);
        if (calendarEndMonth && month > calendarEndMonth) {
            break;
        }
        months.push(month);
    }
    return months;
}

/**
 * Determines the initial month to display in the calendar based on the provided
 * props.
 *
 * This function calculates the starting month, considering constraints such as
 * `startMonth`, `endMonth`, and the number of months to display.
 *
 * @param props The DayPicker props, including navigation and date constraints.
 * @param dateLib The date library to use for date manipulation.
 * @returns The initial month to display.
 */
function getInitialMonth(props, navStart, navEnd, dateLib) {
    const { month, defaultMonth, today = dateLib.today(), numberOfMonths = 1, } = props;
    let initialMonth = month || defaultMonth || today;
    const { differenceInCalendarMonths, addMonths, startOfMonth } = dateLib;
    if (navEnd &&
        differenceInCalendarMonths(navEnd, initialMonth) < numberOfMonths - 1) {
        const offset = -1 * (numberOfMonths - 1);
        initialMonth = addMonths(navEnd, offset);
    }
    if (navStart && differenceInCalendarMonths(initialMonth, navStart) < 0) {
        initialMonth = navStart;
    }
    return startOfMonth(initialMonth);
}

/**
 * Returns the months to display in the calendar.
 *
 * This function generates `CalendarMonth` objects for each month to be
 * displayed, including their weeks and days, based on the provided display
 * months and dates.
 *
 * @param displayMonths The months (as dates) to display in the calendar.
 * @param dates The dates to display in the calendar.
 * @param props Options from the DayPicker props context.
 * @param dateLib The date library to use for date manipulation.
 * @returns An array of `CalendarMonth` objects representing the months to
 *   display.
 */
function getMonths(displayMonths, dates, props, dateLib) {
    const { addDays, endOfBroadcastWeek, endOfISOWeek, endOfMonth, endOfWeek, getISOWeek, getWeek, startOfBroadcastWeek, startOfISOWeek, startOfWeek, } = dateLib;
    const dayPickerMonths = displayMonths.reduce((months, month) => {
        const firstDateOfFirstWeek = props.broadcastCalendar
            ? startOfBroadcastWeek(month, dateLib)
            : props.ISOWeek
                ? startOfISOWeek(month)
                : startOfWeek(month);
        const lastDateOfLastWeek = props.broadcastCalendar
            ? endOfBroadcastWeek(month)
            : props.ISOWeek
                ? endOfISOWeek(endOfMonth(month))
                : endOfWeek(endOfMonth(month));
        /** The dates to display in the month. */
        const monthDates = dates.filter((date) => {
            return date >= firstDateOfFirstWeek && date <= lastDateOfLastWeek;
        });
        const nrOfDaysWithFixedWeeks = props.broadcastCalendar ? 35 : 42;
        if (props.fixedWeeks && monthDates.length < nrOfDaysWithFixedWeeks) {
            const extraDates = dates.filter((date) => {
                const daysToAdd = nrOfDaysWithFixedWeeks - monthDates.length;
                return (date > lastDateOfLastWeek &&
                    date <= addDays(lastDateOfLastWeek, daysToAdd));
            });
            monthDates.push(...extraDates);
        }
        const weeks = monthDates.reduce((weeks, date) => {
            const weekNumber = props.ISOWeek ? getISOWeek(date) : getWeek(date);
            const week = weeks.find((week) => week.weekNumber === weekNumber);
            const day = new CalendarDay(date, month, dateLib);
            if (!week) {
                weeks.push(new CalendarWeek(weekNumber, [day]));
            }
            else {
                week.days.push(day);
            }
            return weeks;
        }, []);
        const dayPickerMonth = new CalendarMonth(month, weeks);
        months.push(dayPickerMonth);
        return months;
    }, []);
    if (!props.reverseMonths) {
        return dayPickerMonths;
    }
    else {
        return dayPickerMonths.reverse();
    }
}

/**
 * Returns the start and end months for calendar navigation.
 *
 * @param props The DayPicker props, including navigation and layout options.
 * @param dateLib The date library to use for date manipulation.
 * @returns A tuple containing the start and end months for navigation.
 */
function getNavMonths(props, dateLib) {
    let { startMonth, endMonth } = props;
    const { startOfYear, startOfDay, startOfMonth, endOfMonth, addYears, endOfYear, newDate, today, } = dateLib;
    // Handle deprecated code
    const { fromYear, toYear, fromMonth, toMonth } = props;
    if (!startMonth && fromMonth) {
        startMonth = fromMonth;
    }
    if (!startMonth && fromYear) {
        startMonth = dateLib.newDate(fromYear, 0, 1);
    }
    if (!endMonth && toMonth) {
        endMonth = toMonth;
    }
    if (!endMonth && toYear) {
        endMonth = newDate(toYear, 11, 31);
    }
    const hasYearDropdown = props.captionLayout === "dropdown" ||
        props.captionLayout === "dropdown-years";
    if (startMonth) {
        startMonth = startOfMonth(startMonth);
    }
    else if (fromYear) {
        startMonth = newDate(fromYear, 0, 1);
    }
    else if (!startMonth && hasYearDropdown) {
        startMonth = startOfYear(addYears(props.today ?? today(), -100));
    }
    if (endMonth) {
        endMonth = endOfMonth(endMonth);
    }
    else if (toYear) {
        endMonth = newDate(toYear, 11, 31);
    }
    else if (!endMonth && hasYearDropdown) {
        endMonth = endOfYear(props.today ?? today());
    }
    return [
        startMonth ? startOfDay(startMonth) : startMonth,
        endMonth ? startOfDay(endMonth) : endMonth,
    ];
}

/**
 * Returns the next month the user can navigate to, based on the given options.
 *
 * The next month is not always the next calendar month:
 *
 * - If it is after the `calendarEndMonth`, it returns `undefined`.
 * - If paged navigation is enabled, it skips forward by the number of displayed
 *   months.
 *
 * @param firstDisplayedMonth The first month currently displayed in the
 *   calendar.
 * @param calendarEndMonth The latest month the user can navigate to.
 * @param options Navigation options, including `numberOfMonths` and
 *   `pagedNavigation`.
 * @param dateLib The date library to use for date manipulation.
 * @returns The next month, or `undefined` if navigation is not possible.
 */
function getNextMonth(firstDisplayedMonth, calendarEndMonth, options, dateLib) {
    if (options.disableNavigation) {
        return undefined;
    }
    const { pagedNavigation, numberOfMonths = 1 } = options;
    const { startOfMonth, addMonths, differenceInCalendarMonths } = dateLib;
    const offset = pagedNavigation ? numberOfMonths : 1;
    const month = startOfMonth(firstDisplayedMonth);
    if (!calendarEndMonth) {
        return addMonths(month, offset);
    }
    const monthsDiff = differenceInCalendarMonths(calendarEndMonth, firstDisplayedMonth);
    if (monthsDiff < numberOfMonths) {
        return undefined;
    }
    return addMonths(month, offset);
}

/**
 * Returns the previous month the user can navigate to, based on the given
 * options.
 *
 * The previous month is not always the previous calendar month:
 *
 * - If it is before the `calendarStartMonth`, it returns `undefined`.
 * - If paged navigation is enabled, it skips back by the number of displayed
 *   months.
 *
 * @param firstDisplayedMonth The first month currently displayed in the
 *   calendar.
 * @param calendarStartMonth The earliest month the user can navigate to.
 * @param options Navigation options, including `numberOfMonths` and
 *   `pagedNavigation`.
 * @param dateLib The date library to use for date manipulation.
 * @returns The previous month, or `undefined` if navigation is not possible.
 */
function getPreviousMonth(firstDisplayedMonth, calendarStartMonth, options, dateLib) {
    if (options.disableNavigation) {
        return undefined;
    }
    const { pagedNavigation, numberOfMonths } = options;
    const { startOfMonth, addMonths, differenceInCalendarMonths } = dateLib;
    const offset = pagedNavigation ? (numberOfMonths ?? 1) : 1;
    const month = startOfMonth(firstDisplayedMonth);
    if (!calendarStartMonth) {
        return addMonths(month, -offset);
    }
    const monthsDiff = differenceInCalendarMonths(month, calendarStartMonth);
    if (monthsDiff <= 0) {
        return undefined;
    }
    return addMonths(month, -offset);
}

/**
 * Returns an array of calendar weeks from an array of calendar months.
 *
 * @param months The array of calendar months.
 * @returns An array of calendar weeks.
 */
function getWeeks(months) {
    const initialWeeks = [];
    return months.reduce((weeks, month) => {
        return weeks.concat(month.weeks.slice());
    }, initialWeeks.slice());
}

/**
 * A custom hook for managing both controlled and uncontrolled component states.
 *
 * This hook allows a component to support both controlled and uncontrolled
 * states by determining whether the `controlledValue` is provided. If it is
 * undefined, the hook falls back to using the internal state.
 *
 * @example
 *   // Uncontrolled usage
 *   const [value, setValue] = useControlledValue(0, undefined);
 *
 *   // Controlled usage
 *   const [value, setValue] = useControlledValue(0, props.value);
 *
 * @template T - The type of the value.
 * @param defaultValue The initial value for the uncontrolled state.
 * @param controlledValue The value for the controlled state. If undefined, the
 *   component will use the uncontrolled state.
 * @returns A tuple where the first element is the current value (either
 *   controlled or uncontrolled) and the second element is a setter function to
 *   update the value.
 */
function useControlledValue(defaultValue, controlledValue) {
    const [uncontrolledValue, setValue] = __mf_38(defaultValue);
    const value = controlledValue === undefined ? uncontrolledValue : controlledValue;
    return [value, setValue];
}

/**
 * Provides the calendar object to work with the calendar in custom components.
 *
 * @private
 * @param props - The DayPicker props related to calendar configuration.
 * @param dateLib - The date utility library instance.
 * @returns The calendar object containing displayed days, weeks, months, and
 *   navigation methods.
 */
function useCalendar(props, dateLib) {
    const [navStart, navEnd] = getNavMonths(props, dateLib);
    const { startOfMonth, endOfMonth } = dateLib;
    const initialMonth = getInitialMonth(props, navStart, navEnd, dateLib);
    const [firstMonth, setFirstMonth] = useControlledValue(initialMonth, 
    // initialMonth is always computed from props.month if provided
    props.month ? initialMonth : undefined);
    // biome-ignore lint/correctness/useExhaustiveDependencies: change the initial month when the time zone changes.
    __mf_28(() => {
        const newInitialMonth = getInitialMonth(props, navStart, navEnd, dateLib);
        setFirstMonth(newInitialMonth);
    }, [props.timeZone]);
    /** The months displayed in the calendar. */
    // biome-ignore lint/correctness/useExhaustiveDependencies: We want to recompute only when specific props change.
    const { months, weeks, days, previousMonth, nextMonth } = __mf_34(() => {
        const displayMonths = getDisplayMonths(firstMonth, navEnd, { numberOfMonths: props.numberOfMonths }, dateLib);
        const dates = getDates(displayMonths, props.endMonth ? endOfMonth(props.endMonth) : undefined, {
            ISOWeek: props.ISOWeek,
            fixedWeeks: props.fixedWeeks,
            broadcastCalendar: props.broadcastCalendar,
        }, dateLib);
        const months = getMonths(displayMonths, dates, {
            broadcastCalendar: props.broadcastCalendar,
            fixedWeeks: props.fixedWeeks,
            ISOWeek: props.ISOWeek,
            reverseMonths: props.reverseMonths,
        }, dateLib);
        const weeks = getWeeks(months);
        const days = getDays(months);
        const previousMonth = getPreviousMonth(firstMonth, navStart, props, dateLib);
        const nextMonth = getNextMonth(firstMonth, navEnd, props, dateLib);
        return {
            months,
            weeks,
            days,
            previousMonth,
            nextMonth,
        };
    }, [
        dateLib,
        firstMonth.getTime(),
        navEnd?.getTime(),
        navStart?.getTime(),
        props.disableNavigation,
        props.broadcastCalendar,
        props.endMonth?.getTime(),
        props.fixedWeeks,
        props.ISOWeek,
        props.numberOfMonths,
        props.pagedNavigation,
        props.reverseMonths,
    ]);
    const { disableNavigation, onMonthChange } = props;
    const isDayInCalendar = (day) => weeks.some((week) => week.days.some((d) => d.isEqualTo(day)));
    const goToMonth = (date) => {
        if (disableNavigation) {
            return;
        }
        let newMonth = startOfMonth(date);
        // if month is before start, use the first month instead
        if (navStart && newMonth < startOfMonth(navStart)) {
            newMonth = startOfMonth(navStart);
        }
        // if month is after endMonth, use the last month instead
        if (navEnd && newMonth > startOfMonth(navEnd)) {
            newMonth = startOfMonth(navEnd);
        }
        setFirstMonth(newMonth);
        onMonthChange?.(newMonth);
    };
    const goToDay = (day) => {
        // is this check necessary?
        if (isDayInCalendar(day)) {
            return;
        }
        goToMonth(day.date);
    };
    const calendar = {
        months,
        weeks,
        days,
        navStart,
        navEnd,
        previousMonth,
        nextMonth,
        goToMonth,
        goToDay,
    };
    return calendar;
}

var FocusTargetPriority;
(function (FocusTargetPriority) {
    FocusTargetPriority[FocusTargetPriority["Today"] = 0] = "Today";
    FocusTargetPriority[FocusTargetPriority["Selected"] = 1] = "Selected";
    FocusTargetPriority[FocusTargetPriority["LastFocused"] = 2] = "LastFocused";
    FocusTargetPriority[FocusTargetPriority["FocusedModifier"] = 3] = "FocusedModifier";
})(FocusTargetPriority || (FocusTargetPriority = {}));
/**
 * Determines if a day is focusable based on its modifiers.
 *
 * A day is considered focusable if it is not disabled, hidden, or outside the
 * displayed month.
 *
 * @param modifiers The modifiers applied to the day.
 * @returns `true` if the day is focusable, otherwise `false`.
 */
function isFocusableDay(modifiers) {
    return (!modifiers[DayFlag.disabled] &&
        !modifiers[DayFlag.hidden] &&
        !modifiers[DayFlag.outside]);
}
/**
 * Calculates the focus target day based on priority.
 *
 * This function determines the day that should receive focus in the calendar,
 * prioritizing days with specific modifiers (e.g., "focused", "today") or
 * selection states.
 *
 * @param days The array of `CalendarDay` objects to evaluate.
 * @param getModifiers A function to retrieve the modifiers for a given day.
 * @param isSelected A function to determine if a day is selected.
 * @param lastFocused The last focused day, if any.
 * @returns The `CalendarDay` that should receive focus, or `undefined` if no
 *   focusable day is found.
 */
function calculateFocusTarget(days, getModifiers, isSelected, lastFocused) {
    let focusTarget;
    let foundFocusTargetPriority = -1;
    for (const day of days) {
        const modifiers = getModifiers(day);
        if (isFocusableDay(modifiers)) {
            if (modifiers[DayFlag.focused] &&
                foundFocusTargetPriority < FocusTargetPriority.FocusedModifier) {
                focusTarget = day;
                foundFocusTargetPriority = FocusTargetPriority.FocusedModifier;
            }
            else if (lastFocused?.isEqualTo(day) &&
                foundFocusTargetPriority < FocusTargetPriority.LastFocused) {
                focusTarget = day;
                foundFocusTargetPriority = FocusTargetPriority.LastFocused;
            }
            else if (isSelected(day.date) &&
                foundFocusTargetPriority < FocusTargetPriority.Selected) {
                focusTarget = day;
                foundFocusTargetPriority = FocusTargetPriority.Selected;
            }
            else if (modifiers[DayFlag.today] &&
                foundFocusTargetPriority < FocusTargetPriority.Today) {
                focusTarget = day;
                foundFocusTargetPriority = FocusTargetPriority.Today;
            }
        }
    }
    if (!focusTarget) {
        // Return the first day that is focusable
        focusTarget = days.find((day) => isFocusableDay(getModifiers(day)));
    }
    return focusTarget;
}

/**
 * Calculates the next date that should be focused in the calendar.
 *
 * This function determines the next focusable date based on the movement
 * direction, constraints, and calendar configuration.
 *
 * @param moveBy The unit of movement (e.g., "day", "week").
 * @param moveDir The direction of movement ("before" or "after").
 * @param refDate The reference date from which to calculate the next focusable
 *   date.
 * @param navStart The earliest date the user can navigate to.
 * @param navEnd The latest date the user can navigate to.
 * @param props The DayPicker props, including calendar configuration options.
 * @param dateLib The date library to use for date manipulation.
 * @returns The next focusable date.
 */
function getFocusableDate(moveBy, moveDir, refDate, navStart, navEnd, props, dateLib) {
    const { ISOWeek, broadcastCalendar } = props;
    const { addDays, addMonths, addWeeks, addYears, endOfBroadcastWeek, endOfISOWeek, endOfWeek, max, min, startOfBroadcastWeek, startOfISOWeek, startOfWeek, } = dateLib;
    const moveFns = {
        day: addDays,
        week: addWeeks,
        month: addMonths,
        year: addYears,
        startOfWeek: (date) => broadcastCalendar
            ? startOfBroadcastWeek(date, dateLib)
            : ISOWeek
                ? startOfISOWeek(date)
                : startOfWeek(date),
        endOfWeek: (date) => broadcastCalendar
            ? endOfBroadcastWeek(date)
            : ISOWeek
                ? endOfISOWeek(date)
                : endOfWeek(date),
    };
    let focusableDate = moveFns[moveBy](refDate, moveDir === "after" ? 1 : -1);
    if (moveDir === "before" && navStart) {
        focusableDate = max([navStart, focusableDate]);
    }
    else if (moveDir === "after" && navEnd) {
        focusableDate = min([navEnd, focusableDate]);
    }
    return focusableDate;
}

/**
 * Determines the next focusable day in the calendar.
 *
 * This function recursively calculates the next focusable day based on the
 * movement direction and modifiers applied to the days.
 *
 * @param moveBy The unit of movement (e.g., "day", "week").
 * @param moveDir The direction of movement ("before" or "after").
 * @param refDay The currently focused day.
 * @param calendarStartMonth The earliest month the user can navigate to.
 * @param calendarEndMonth The latest month the user can navigate to.
 * @param props The DayPicker props, including modifiers and configuration
 *   options.
 * @param dateLib The date library to use for date manipulation.
 * @param attempt The current recursion attempt (used to limit recursion depth).
 * @returns The next focusable day, or `undefined` if no focusable day is found.
 */
function getNextFocus(moveBy, moveDir, refDay, calendarStartMonth, calendarEndMonth, props, dateLib, attempt = 0) {
    if (attempt > 365) {
        // Limit the recursion to 365 attempts
        return undefined;
    }
    const focusableDate = getFocusableDate(moveBy, moveDir, refDay.date, calendarStartMonth, calendarEndMonth, props, dateLib);
    const isDisabled = Boolean(props.disabled &&
        dateMatchModifiers(focusableDate, props.disabled, dateLib));
    const isHidden = Boolean(props.hidden && dateMatchModifiers(focusableDate, props.hidden, dateLib));
    const targetMonth = focusableDate;
    const focusDay = new CalendarDay(focusableDate, targetMonth, dateLib);
    if (!isDisabled && !isHidden) {
        return focusDay;
    }
    // Recursively attempt to find the next focusable date
    return getNextFocus(moveBy, moveDir, focusDay, calendarStartMonth, calendarEndMonth, props, dateLib, attempt + 1);
}

/**
 * Manages focus behavior for the DayPicker component, including setting,
 * moving, and blurring focus on calendar days.
 *
 * @template T - The type of DayPicker props.
 * @param props - The DayPicker props.
 * @param calendar - The calendar object containing the displayed days and
 *   months.
 * @param getModifiers - A function to retrieve modifiers for a given day.
 * @param isSelected - A function to check if a date is selected.
 * @param dateLib - The date utility library instance.
 * @returns An object containing focus-related methods and the currently focused
 *   day.
 */
function useFocus(props, calendar, getModifiers, isSelected, dateLib) {
    const { autoFocus } = props;
    const [lastFocused, setLastFocused] = __mf_38();
    const focusTarget = calculateFocusTarget(calendar.days, getModifiers, isSelected || (() => false), lastFocused);
    const [focusedDay, setFocused] = __mf_38(autoFocus ? focusTarget : undefined);
    const blur = () => {
        setLastFocused(focusedDay);
        setFocused(undefined);
    };
    const moveFocus = (moveBy, moveDir) => {
        if (!focusedDay)
            return;
        const nextFocus = getNextFocus(moveBy, moveDir, focusedDay, calendar.navStart, calendar.navEnd, props, dateLib);
        if (!nextFocus)
            return;
        if (props.disableNavigation) {
            const isNextInCalendar = calendar.days.some((day) => day.isEqualTo(nextFocus));
            if (!isNextInCalendar) {
                return;
            }
        }
        calendar.goToDay(nextFocus);
        setFocused(nextFocus);
    };
    const isFocusTarget = (day) => {
        return Boolean(focusTarget?.isEqualTo(day));
    };
    const useFocus = {
        isFocusTarget,
        setFocused,
        focused: focusedDay,
        blur,
        moveFocus,
    };
    return useFocus;
}

/**
 * Hook to manage multiple-date selection in the DayPicker component.
 *
 * @template T - The type of DayPicker props.
 * @param props - The DayPicker props.
 * @param dateLib - The date utility library instance.
 * @returns An object containing the selected dates, a function to select dates,
 *   and a function to check if a date is selected.
 */
function useMulti(props, dateLib) {
    const { selected: initiallySelected, required, onSelect, } = props;
    const [internallySelected, setSelected] = useControlledValue(initiallySelected, onSelect ? initiallySelected : undefined);
    const selected = !onSelect ? internallySelected : initiallySelected;
    const { isSameDay } = dateLib;
    const isSelected = (date) => {
        return selected?.some((d) => isSameDay(d, date)) ?? false;
    };
    const { min, max } = props;
    const select = (triggerDate, modifiers, e) => {
        let newDates = [...(selected ?? [])];
        if (isSelected(triggerDate)) {
            if (selected?.length === min) {
                // Min value reached, do nothing
                return;
            }
            if (required && selected?.length === 1) {
                // Required value already selected do nothing
                return;
            }
            newDates = selected?.filter((d) => !isSameDay(d, triggerDate));
        }
        else {
            if (selected?.length === max) {
                // Max value reached, reset the selection to date
                newDates = [triggerDate];
            }
            else {
                // Add the date to the selection
                newDates = [...newDates, triggerDate];
            }
        }
        if (!onSelect) {
            setSelected(newDates);
        }
        onSelect?.(newDates, triggerDate, modifiers, e);
        return newDates;
    };
    return {
        selected,
        select,
        isSelected,
    };
}

/**
 * Adds a date to an existing range, considering constraints like minimum and
 * maximum range size.
 *
 * @param date - The date to add to the range.
 * @param initialRange - The initial range to which the date will be added.
 * @param min - The minimum number of days in the range.
 * @param max - The maximum number of days in the range.
 * @param required - Whether the range must always include at least one date.
 * @param dateLib - The date utility library instance.
 * @returns The updated date range, or `undefined` if the range is cleared.
 * @group Utilities
 */
function addToRange(date, initialRange, min = 0, max = 0, required = false, dateLib = defaultDateLib) {
    const { from, to } = initialRange || {};
    const { isSameDay, isAfter, isBefore } = dateLib;
    let range;
    if (!from && !to) {
        // the range is empty, add the date
        range = { from: date, to: min > 0 ? undefined : date };
    }
    else if (from && !to) {
        // adding date to an incomplete range
        if (isSameDay(from, date)) {
            // adding a date equal to the start of the range
            if (min === 0) {
                range = { from, to: date };
            }
            else if (required) {
                range = { from, to: undefined };
            }
            else {
                range = undefined;
            }
        }
        else if (isBefore(date, from)) {
            // adding a date before the start of the range
            range = { from: date, to: from };
        }
        else {
            // adding a date after the start of the range
            range = { from, to: date };
        }
    }
    else if (from && to) {
        // adding date to a complete range
        if (isSameDay(from, date) && isSameDay(to, date)) {
            // adding a date that is equal to both start and end of the range
            if (required) {
                range = { from, to };
            }
            else {
                range = undefined;
            }
        }
        else if (isSameDay(from, date)) {
            // adding a date equal to the the start of the range
            range = { from, to: min > 0 ? undefined : date };
        }
        else if (isSameDay(to, date)) {
            // adding a dare equal to the end of the range
            range = { from: date, to: min > 0 ? undefined : date };
        }
        else if (isBefore(date, from)) {
            // adding a date before the start of the range
            range = { from: date, to: to };
        }
        else if (isAfter(date, from)) {
            // adding a date after the start of the range
            range = { from, to: date };
        }
        else if (isAfter(date, to)) {
            // adding a date after the end of the range
            range = { from, to: date };
        }
        else {
            throw new Error("Invalid range");
        }
    }
    // check for min / max
    if (range?.from && range?.to) {
        const diff = dateLib.differenceInCalendarDays(range.to, range.from);
        if (max > 0 && diff > max) {
            range = { from: date, to: undefined };
        }
        else if (min > 1 && diff < min) {
            range = { from: date, to: undefined };
        }
    }
    return range;
}

/**
 * Checks if a date range contains one or more specified days of the week.
 *
 * @since 9.2.2
 * @param range - The date range to check.
 * @param dayOfWeek - The day(s) of the week to check for (`0-6`, where `0` is
 *   Sunday).
 * @param dateLib - The date utility library instance.
 * @returns `true` if the range contains the specified day(s) of the week,
 *   otherwise `false`.
 * @group Utilities
 */
function rangeContainsDayOfWeek(range, dayOfWeek, dateLib = defaultDateLib) {
    const dayOfWeekArr = !Array.isArray(dayOfWeek) ? [dayOfWeek] : dayOfWeek;
    let date = range.from;
    const totalDays = dateLib.differenceInCalendarDays(range.to, range.from);
    // iterate at maximum one week or the total days if the range is shorter than one week
    const totalDaysLimit = Math.min(totalDays, 6);
    for (let i = 0; i <= totalDaysLimit; i++) {
        if (dayOfWeekArr.includes(date.getDay())) {
            return true;
        }
        date = dateLib.addDays(date, 1);
    }
    return false;
}

/**
 * Determines if two date ranges overlap.
 *
 * @since 9.2.2
 * @param rangeLeft - The first date range.
 * @param rangeRight - The second date range.
 * @param dateLib - The date utility library instance.
 * @returns `true` if the ranges overlap, otherwise `false`.
 * @group Utilities
 */
function rangeOverlaps(rangeLeft, rangeRight, dateLib = defaultDateLib) {
    return (rangeIncludesDate(rangeLeft, rangeRight.from, false, dateLib) ||
        rangeIncludesDate(rangeLeft, rangeRight.to, false, dateLib) ||
        rangeIncludesDate(rangeRight, rangeLeft.from, false, dateLib) ||
        rangeIncludesDate(rangeRight, rangeLeft.to, false, dateLib));
}

/**
 * Checks if a date range contains dates that match the given modifiers.
 *
 * @since 9.2.2
 * @param range - The date range to check.
 * @param modifiers - The modifiers to match against.
 * @param dateLib - The date utility library instance.
 * @returns `true` if the range contains matching dates, otherwise `false`.
 * @group Utilities
 */
function rangeContainsModifiers(range, modifiers, dateLib = defaultDateLib) {
    const matchers = Array.isArray(modifiers) ? modifiers : [modifiers];
    // Defer function matchers evaluation as they are the least performant.
    const nonFunctionMatchers = matchers.filter((matcher) => typeof matcher !== "function");
    const nonFunctionMatchersResult = nonFunctionMatchers.some((matcher) => {
        if (typeof matcher === "boolean")
            return matcher;
        if (dateLib.isDate(matcher)) {
            return rangeIncludesDate(range, matcher, false, dateLib);
        }
        if (isDatesArray(matcher, dateLib)) {
            return matcher.some((date) => rangeIncludesDate(range, date, false, dateLib));
        }
        if (isDateRange(matcher)) {
            if (matcher.from && matcher.to) {
                return rangeOverlaps(range, { from: matcher.from, to: matcher.to }, dateLib);
            }
            return false;
        }
        if (isDayOfWeekType(matcher)) {
            return rangeContainsDayOfWeek(range, matcher.dayOfWeek, dateLib);
        }
        if (isDateInterval(matcher)) {
            const isClosedInterval = dateLib.isAfter(matcher.before, matcher.after);
            if (isClosedInterval) {
                return rangeOverlaps(range, {
                    from: dateLib.addDays(matcher.after, 1),
                    to: dateLib.addDays(matcher.before, -1),
                }, dateLib);
            }
            return (dateMatchModifiers(range.from, matcher, dateLib) ||
                dateMatchModifiers(range.to, matcher, dateLib));
        }
        if (isDateAfterType(matcher) || isDateBeforeType(matcher)) {
            return (dateMatchModifiers(range.from, matcher, dateLib) ||
                dateMatchModifiers(range.to, matcher, dateLib));
        }
        return false;
    });
    if (nonFunctionMatchersResult) {
        return true;
    }
    const functionMatchers = matchers.filter((matcher) => typeof matcher === "function");
    if (functionMatchers.length) {
        let date = range.from;
        const totalDays = dateLib.differenceInCalendarDays(range.to, range.from);
        for (let i = 0; i <= totalDays; i++) {
            if (functionMatchers.some((matcher) => matcher(date))) {
                return true;
            }
            date = dateLib.addDays(date, 1);
        }
    }
    return false;
}

/**
 * Hook to manage range selection in the DayPicker component.
 *
 * @template T - The type of DayPicker props.
 * @param props - The DayPicker props.
 * @param dateLib - The date utility library instance.
 * @returns An object containing the selected range, a function to select a
 *   range, and a function to check if a date is within the range.
 */
function useRange(props, dateLib) {
    const { disabled, excludeDisabled, resetOnSelect, selected: initiallySelected, required, onSelect, } = props;
    const [internallySelected, setSelected] = useControlledValue(initiallySelected, onSelect ? initiallySelected : undefined);
    const selected = !onSelect ? internallySelected : initiallySelected;
    const isSelected = (date) => selected && rangeIncludesDate(selected, date, false, dateLib);
    const select = (triggerDate, modifiers, e) => {
        const { min, max } = props;
        let newRange;
        if (triggerDate) {
            const selectedFrom = selected?.from;
            const selectedTo = selected?.to;
            const hasFullRange = !!selectedFrom && !!selectedTo;
            const isClickingSingleDayRange = !!selectedFrom &&
                !!selectedTo &&
                dateLib.isSameDay(selectedFrom, selectedTo) &&
                dateLib.isSameDay(triggerDate, selectedFrom);
            if (resetOnSelect && (hasFullRange || !selected?.from)) {
                if (!required && isClickingSingleDayRange) {
                    newRange = undefined;
                }
                else {
                    newRange = { from: triggerDate, to: undefined };
                }
            }
            else {
                newRange = addToRange(triggerDate, selected, min, max, required, dateLib);
            }
        }
        if (excludeDisabled && disabled && newRange?.from && newRange.to) {
            if (rangeContainsModifiers({ from: newRange.from, to: newRange.to }, disabled, dateLib)) {
                // if a disabled days is found, the range is reset
                newRange.from = triggerDate;
                newRange.to = undefined;
            }
        }
        if (!onSelect) {
            setSelected(newRange);
        }
        onSelect?.(newRange, triggerDate, modifiers, e);
        return newRange;
    };
    return {
        selected,
        select,
        isSelected,
    };
}

/**
 * Hook to manage single-date selection in the DayPicker component.
 *
 * @template T - The type of DayPicker props.
 * @param props - The DayPicker props.
 * @param dateLib - The date utility library instance.
 * @returns An object containing the selected date, a function to select a date,
 *   and a function to check if a date is selected.
 */
function useSingle(props, dateLib) {
    const { selected: initiallySelected, required, onSelect, } = props;
    const [internallySelected, setSelected] = useControlledValue(initiallySelected, onSelect ? initiallySelected : undefined);
    const selected = !onSelect ? internallySelected : initiallySelected;
    const { isSameDay } = dateLib;
    const isSelected = (compareDate) => {
        return selected ? isSameDay(selected, compareDate) : false;
    };
    const select = (triggerDate, modifiers, e) => {
        let newDate = triggerDate;
        if (!required && selected && selected && isSameDay(triggerDate, selected)) {
            // If the date is the same, clear the selection.
            newDate = undefined;
        }
        if (!onSelect) {
            setSelected(newDate);
        }
        if (required) {
            onSelect?.(newDate, triggerDate, modifiers, e);
        }
        else {
            onSelect?.(newDate, triggerDate, modifiers, e);
        }
        return newDate;
    };
    return {
        selected,
        select,
        isSelected,
    };
}

/**
 * Determines the appropriate selection hook to use based on the selection mode
 * and returns the corresponding selection object.
 *
 * @template T - The type of DayPicker props.
 * @param props - The DayPicker props.
 * @param dateLib - The date utility library instance.
 * @returns The selection object for the specified mode, or `undefined` if no
 *   mode is set.
 */
function useSelection(props, dateLib) {
    const single = useSingle(props, dateLib);
    const multi = useMulti(props, dateLib);
    const range = useRange(props, dateLib);
    switch (props.mode) {
        case "single":
            return single;
        case "multiple":
            return multi;
        case "range":
            return range;
        default:
            return undefined;
    }
}

/**
 * Convert a {@link Date} or {@link TZDate} instance to the given time zone.
 * Reuses the same instance when it is already a {@link TZDate} using the target
 * time zone to avoid extra allocations.
 */
function toTimeZone(date, timeZone) {
    if (date instanceof TZDate && date.timeZone === timeZone) {
        return date;
    }
    return new TZDate(date, timeZone);
}

function toZoneNoon(date, timeZone, noonSafe) {
    return toTimeZone(date, timeZone);
}
function convertMatcher(matcher, timeZone, noonSafe) {
    if (typeof matcher === "boolean" || typeof matcher === "function") {
        return matcher;
    }
    if (matcher instanceof Date) {
        return toZoneNoon(matcher, timeZone);
    }
    if (Array.isArray(matcher)) {
        return matcher.map((value) => value instanceof Date ? toZoneNoon(value, timeZone) : value);
    }
    if (isDateRange(matcher)) {
        return {
            ...matcher,
            from: matcher.from ? toTimeZone(matcher.from, timeZone) : matcher.from,
            to: matcher.to ? toTimeZone(matcher.to, timeZone) : matcher.to,
        };
    }
    if (isDateInterval(matcher)) {
        return {
            before: toZoneNoon(matcher.before, timeZone),
            after: toZoneNoon(matcher.after, timeZone),
        };
    }
    if (isDateAfterType(matcher)) {
        return {
            after: toZoneNoon(matcher.after, timeZone),
        };
    }
    if (isDateBeforeType(matcher)) {
        return {
            before: toZoneNoon(matcher.before, timeZone),
        };
    }
    return matcher;
}
/**
 * Convert any {@link Matcher} or array of matchers to the specified time zone.
 *
 * @param matchers - The matcher or matchers to convert.
 * @param timeZone - The target IANA time zone.
 * @returns The converted matcher(s).
 * @group Utilities
 */
function convertMatchersToTimeZone(matchers, timeZone, noonSafe) {
    if (!matchers) {
        return matchers;
    }
    if (Array.isArray(matchers)) {
        return matchers.map((matcher) => convertMatcher(matcher, timeZone));
    }
    return convertMatcher(matchers, timeZone);
}

/**
 * Renders the DayPicker calendar component.
 *
 * @param initialProps - The props for the DayPicker component.
 * @returns The rendered DayPicker component.
 * @group DayPicker
 * @see https://daypicker.dev
 */
function DayPicker(initialProps) {
    let props = initialProps;
    const timeZone = props.timeZone;
    if (timeZone) {
        props = {
            ...initialProps,
            timeZone,
        };
        if (props.today) {
            props.today = toTimeZone(props.today, timeZone);
        }
        if (props.month) {
            props.month = toTimeZone(props.month, timeZone);
        }
        if (props.defaultMonth) {
            props.defaultMonth = toTimeZone(props.defaultMonth, timeZone);
        }
        if (props.startMonth) {
            props.startMonth = toTimeZone(props.startMonth, timeZone);
        }
        if (props.endMonth) {
            props.endMonth = toTimeZone(props.endMonth, timeZone);
        }
        if (props.mode === "single" && props.selected) {
            props.selected = toTimeZone(props.selected, timeZone);
        }
        else if (props.mode === "multiple" && props.selected) {
            props.selected = props.selected?.map((date) => toTimeZone(date, timeZone));
        }
        else if (props.mode === "range" && props.selected) {
            props.selected = {
                from: props.selected.from
                    ? toTimeZone(props.selected.from, timeZone)
                    : props.selected.from,
                to: props.selected.to
                    ? toTimeZone(props.selected.to, timeZone)
                    : props.selected.to,
            };
        }
        if (props.disabled !== undefined) {
            props.disabled = convertMatchersToTimeZone(props.disabled, timeZone);
        }
        if (props.hidden !== undefined) {
            props.hidden = convertMatchersToTimeZone(props.hidden, timeZone);
        }
        if (props.modifiers) {
            const nextModifiers = {};
            Object.keys(props.modifiers).forEach((key) => {
                nextModifiers[key] = convertMatchersToTimeZone(props.modifiers?.[key], timeZone);
            });
            props.modifiers = nextModifiers;
        }
    }
    const { components, formatters, labels, dateLib, locale, classNames } = __mf_34(() => {
        const locale = { ...enUS, ...props.locale };
        const weekStartsOn = props.broadcastCalendar ? 1 : props.weekStartsOn;
        const noonOverrides = props.noonSafe && props.timeZone
            ? createNoonOverrides(props.timeZone, {
                weekStartsOn,
                locale,
            })
            : undefined;
        const overrides = props.dateLib && noonOverrides
            ? { ...noonOverrides, ...props.dateLib }
            : (props.dateLib ?? noonOverrides);
        const dateLib = new DateLib({
            locale,
            weekStartsOn,
            firstWeekContainsDate: props.firstWeekContainsDate,
            useAdditionalWeekYearTokens: props.useAdditionalWeekYearTokens,
            useAdditionalDayOfYearTokens: props.useAdditionalDayOfYearTokens,
            timeZone: props.timeZone,
            numerals: props.numerals,
        }, overrides);
        return {
            dateLib,
            components: getComponents(props.components),
            formatters: getFormatters(props.formatters),
            labels: getLabels(props.labels, dateLib.options),
            locale,
            classNames: { ...getDefaultClassNames(), ...props.classNames },
        };
    }, [
        props.locale,
        props.broadcastCalendar,
        props.weekStartsOn,
        props.firstWeekContainsDate,
        props.useAdditionalWeekYearTokens,
        props.useAdditionalDayOfYearTokens,
        props.timeZone,
        props.numerals,
        props.dateLib,
        props.noonSafe,
        props.components,
        props.formatters,
        props.labels,
        props.classNames,
    ]);
    if (!props.today) {
        props = { ...props, today: dateLib.today() };
    }
    const { captionLayout, mode, navLayout, numberOfMonths = 1, onDayBlur, onDayClick, onDayFocus, onDayKeyDown, onDayMouseEnter, onDayMouseLeave, onNextClick, onPrevClick, showWeekNumber, styles, } = props;
    const { formatCaption, formatDay, formatMonthDropdown, formatWeekNumber, formatWeekNumberHeader, formatWeekdayName, formatYearDropdown, } = formatters;
    const calendar = useCalendar(props, dateLib);
    const { days, months, navStart, navEnd, previousMonth, nextMonth, goToMonth, } = calendar;
    const getModifiers = createGetModifiers(days, props, navStart, navEnd, dateLib);
    const { isSelected, select, selected: selectedValue, } = useSelection(props, dateLib) ?? {};
    const { blur, focused, isFocusTarget, moveFocus, setFocused } = useFocus(props, calendar, getModifiers, isSelected ?? (() => false), dateLib);
    const { labelDayButton, labelGridcell, labelGrid, labelMonthDropdown, labelNav, labelPrevious, labelNext, labelWeekday, labelWeekNumber, labelWeekNumberHeader, labelYearDropdown, } = labels;
    const weekdays = __mf_34(() => getWeekdays(dateLib, props.ISOWeek, props.broadcastCalendar, props.today), [dateLib, props.ISOWeek, props.broadcastCalendar, props.today]);
    const isInteractive = mode !== undefined || onDayClick !== undefined;
    const handlePreviousClick = __mf_24(() => {
        if (!previousMonth)
            return;
        goToMonth(previousMonth);
        onPrevClick?.(previousMonth);
    }, [previousMonth, goToMonth, onPrevClick]);
    const handleNextClick = __mf_24(() => {
        if (!nextMonth)
            return;
        goToMonth(nextMonth);
        onNextClick?.(nextMonth);
    }, [goToMonth, nextMonth, onNextClick]);
    const handleDayClick = __mf_24((day, m) => (e) => {
        e.preventDefault();
        e.stopPropagation();
        setFocused(day);
        if (m.disabled) {
            return;
        }
        select?.(day.date, m, e);
        onDayClick?.(day.date, m, e);
    }, [select, onDayClick, setFocused]);
    const handleDayFocus = __mf_24((day, m) => (e) => {
        setFocused(day);
        onDayFocus?.(day.date, m, e);
    }, [onDayFocus, setFocused]);
    const handleDayBlur = __mf_24((day, m) => (e) => {
        blur();
        onDayBlur?.(day.date, m, e);
    }, [blur, onDayBlur]);
    const handleDayKeyDown = __mf_24((day, modifiers) => (e) => {
        const keyMap = {
            ArrowLeft: [
                e.shiftKey ? "month" : "day",
                props.dir === "rtl" ? "after" : "before",
            ],
            ArrowRight: [
                e.shiftKey ? "month" : "day",
                props.dir === "rtl" ? "before" : "after",
            ],
            ArrowDown: [e.shiftKey ? "year" : "week", "after"],
            ArrowUp: [e.shiftKey ? "year" : "week", "before"],
            PageUp: [e.shiftKey ? "year" : "month", "before"],
            PageDown: [e.shiftKey ? "year" : "month", "after"],
            Home: ["startOfWeek", "before"],
            End: ["endOfWeek", "after"],
        };
        if (keyMap[e.key]) {
            e.preventDefault();
            e.stopPropagation();
            const [moveBy, moveDir] = keyMap[e.key];
            moveFocus(moveBy, moveDir);
        }
        onDayKeyDown?.(day.date, modifiers, e);
    }, [moveFocus, onDayKeyDown, props.dir]);
    const handleDayMouseEnter = __mf_24((day, modifiers) => (e) => {
        onDayMouseEnter?.(day.date, modifiers, e);
    }, [onDayMouseEnter]);
    const handleDayMouseLeave = __mf_24((day, modifiers) => (e) => {
        onDayMouseLeave?.(day.date, modifiers, e);
    }, [onDayMouseLeave]);
    const handleMonthChange = __mf_24((date) => (e) => {
        const selectedMonth = Number(e.target.value);
        const month = dateLib.setMonth(dateLib.startOfMonth(date), selectedMonth);
        goToMonth(month);
    }, [dateLib, goToMonth]);
    const handleYearChange = __mf_24((date) => (e) => {
        const selectedYear = Number(e.target.value);
        const month = dateLib.setYear(dateLib.startOfMonth(date), selectedYear);
        goToMonth(month);
    }, [dateLib, goToMonth]);
    const { className, style } = __mf_34(() => ({
        className: [classNames[UI.Root], props.className]
            .filter(Boolean)
            .join(" "),
        style: { ...styles?.[UI.Root], ...props.style },
    }), [classNames, props.className, props.style, styles]);
    const dataAttributes = getDataAttributes(props);
    const rootElRef = __mf_37(null);
    useAnimation(rootElRef, Boolean(props.animate), {
        classNames,
        months,
        focused,
        dateLib,
    });
    const contextValue = {
        dayPickerProps: props,
        selected: selectedValue,
        select: select,
        isSelected,
        months,
        nextMonth,
        previousMonth,
        goToMonth,
        getModifiers,
        components,
        classNames,
        styles,
        labels,
        formatters,
    };
    return (React3.createElement(dayPickerContext.Provider, { value: contextValue },
        React3.createElement(components.Root, { rootRef: props.animate ? rootElRef : undefined, className: className, style: style, dir: props.dir, id: props.id, lang: props.lang ?? locale.code, nonce: props.nonce, title: props.title, role: props.role, "aria-label": props["aria-label"], "aria-labelledby": props["aria-labelledby"], ...dataAttributes },
            React3.createElement(components.Months, { className: classNames[UI.Months], style: styles?.[UI.Months] },
                !props.hideNavigation && !navLayout && (React3.createElement(components.Nav, { "data-animated-nav": props.animate ? "true" : undefined, className: classNames[UI.Nav], style: styles?.[UI.Nav], "aria-label": labelNav(), onPreviousClick: handlePreviousClick, onNextClick: handleNextClick, previousMonth: previousMonth, nextMonth: nextMonth })),
                months.map((calendarMonth, displayIndex) => {
                    return (React3.createElement(components.Month, { "data-animated-month": props.animate ? "true" : undefined, className: classNames[UI.Month], style: styles?.[UI.Month], 
                        // biome-ignore lint/suspicious/noArrayIndexKey: breaks animation
                        key: displayIndex, displayIndex: displayIndex, calendarMonth: calendarMonth },
                        navLayout === "around" &&
                            !props.hideNavigation &&
                            displayIndex === 0 && (React3.createElement(components.PreviousMonthButton, { type: "button", className: classNames[UI.PreviousMonthButton], tabIndex: previousMonth ? undefined : -1, "aria-disabled": previousMonth ? undefined : true, "aria-label": labelPrevious(previousMonth), onClick: handlePreviousClick, "data-animated-button": props.animate ? "true" : undefined },
                            React3.createElement(components.Chevron, { disabled: previousMonth ? undefined : true, className: classNames[UI.Chevron], orientation: props.dir === "rtl" ? "right" : "left" }))),
                        React3.createElement(components.MonthCaption, { "data-animated-caption": props.animate ? "true" : undefined, className: classNames[UI.MonthCaption], style: styles?.[UI.MonthCaption], calendarMonth: calendarMonth, displayIndex: displayIndex }, captionLayout?.startsWith("dropdown") ? (React3.createElement(components.DropdownNav, { className: classNames[UI.Dropdowns], style: styles?.[UI.Dropdowns] },
                            (() => {
                                const monthControl = captionLayout === "dropdown" ||
                                    captionLayout === "dropdown-months" ? (React3.createElement(components.MonthsDropdown, { key: "month", className: classNames[UI.MonthsDropdown], "aria-label": labelMonthDropdown(), classNames: classNames, components: components, disabled: Boolean(props.disableNavigation), onChange: handleMonthChange(calendarMonth.date), options: getMonthOptions(calendarMonth.date, navStart, navEnd, formatters, dateLib), style: styles?.[UI.Dropdown], value: dateLib.getMonth(calendarMonth.date) })) : (React3.createElement("span", { key: "month" }, formatMonthDropdown(calendarMonth.date, dateLib)));
                                const yearControl = captionLayout === "dropdown" ||
                                    captionLayout === "dropdown-years" ? (React3.createElement(components.YearsDropdown, { key: "year", className: classNames[UI.YearsDropdown], "aria-label": labelYearDropdown(dateLib.options), classNames: classNames, components: components, disabled: Boolean(props.disableNavigation), onChange: handleYearChange(calendarMonth.date), options: getYearOptions(navStart, navEnd, formatters, dateLib, Boolean(props.reverseYears)), style: styles?.[UI.Dropdown], value: dateLib.getYear(calendarMonth.date) })) : (React3.createElement("span", { key: "year" }, formatYearDropdown(calendarMonth.date, dateLib)));
                                const controls = dateLib.getMonthYearOrder() === "year-first"
                                    ? [yearControl, monthControl]
                                    : [monthControl, yearControl];
                                return controls;
                            })(),
                            React3.createElement("span", { role: "status", "aria-live": "polite", style: {
                                    border: 0,
                                    clip: "rect(0 0 0 0)",
                                    height: "1px",
                                    margin: "-1px",
                                    overflow: "hidden",
                                    padding: 0,
                                    position: "absolute",
                                    width: "1px",
                                    whiteSpace: "nowrap",
                                    wordWrap: "normal",
                                } }, formatCaption(calendarMonth.date, dateLib.options, dateLib)))) : (React3.createElement(components.CaptionLabel, { className: classNames[UI.CaptionLabel], role: "status", "aria-live": "polite" }, formatCaption(calendarMonth.date, dateLib.options, dateLib)))),
                        navLayout === "around" &&
                            !props.hideNavigation &&
                            displayIndex === numberOfMonths - 1 && (React3.createElement(components.NextMonthButton, { type: "button", className: classNames[UI.NextMonthButton], tabIndex: nextMonth ? undefined : -1, "aria-disabled": nextMonth ? undefined : true, "aria-label": labelNext(nextMonth), onClick: handleNextClick, "data-animated-button": props.animate ? "true" : undefined },
                            React3.createElement(components.Chevron, { disabled: nextMonth ? undefined : true, className: classNames[UI.Chevron], orientation: props.dir === "rtl" ? "left" : "right" }))),
                        displayIndex === numberOfMonths - 1 &&
                            navLayout === "after" &&
                            !props.hideNavigation && (React3.createElement(components.Nav, { "data-animated-nav": props.animate ? "true" : undefined, className: classNames[UI.Nav], style: styles?.[UI.Nav], "aria-label": labelNav(), onPreviousClick: handlePreviousClick, onNextClick: handleNextClick, previousMonth: previousMonth, nextMonth: nextMonth })),
                        React3.createElement(components.MonthGrid, { role: "grid", "aria-multiselectable": mode === "multiple" || mode === "range", "aria-label": labelGrid(calendarMonth.date, dateLib.options, dateLib) ||
                                undefined, className: classNames[UI.MonthGrid], style: styles?.[UI.MonthGrid] },
                            !props.hideWeekdays && (React3.createElement(components.Weekdays, { "data-animated-weekdays": props.animate ? "true" : undefined, className: classNames[UI.Weekdays], style: styles?.[UI.Weekdays] },
                                showWeekNumber && (React3.createElement(components.WeekNumberHeader, { "aria-label": labelWeekNumberHeader(dateLib.options), className: classNames[UI.WeekNumberHeader], style: styles?.[UI.WeekNumberHeader], scope: "col" }, formatWeekNumberHeader())),
                                weekdays.map((weekday) => (React3.createElement(components.Weekday, { "aria-label": labelWeekday(weekday, dateLib.options, dateLib), className: classNames[UI.Weekday], key: String(weekday), style: styles?.[UI.Weekday], scope: "col" }, formatWeekdayName(weekday, dateLib.options, dateLib)))))),
                            React3.createElement(components.Weeks, { "data-animated-weeks": props.animate ? "true" : undefined, className: classNames[UI.Weeks], style: styles?.[UI.Weeks] }, calendarMonth.weeks.map((week) => {
                                return (React3.createElement(components.Week, { className: classNames[UI.Week], key: week.weekNumber, style: styles?.[UI.Week], week: week },
                                    showWeekNumber && (React3.createElement(components.WeekNumber, { week: week, style: styles?.[UI.WeekNumber], "aria-label": labelWeekNumber(week.weekNumber, {
                                            locale,
                                        }), className: classNames[UI.WeekNumber], scope: "row", role: "rowheader" }, formatWeekNumber(week.weekNumber, dateLib))),
                                    week.days.map((day) => {
                                        const { date } = day;
                                        const modifiers = getModifiers(day);
                                        modifiers[DayFlag.focused] =
                                            !modifiers.hidden &&
                                                Boolean(focused?.isEqualTo(day));
                                        modifiers[SelectionState.selected] =
                                            isSelected?.(date) || modifiers.selected;
                                        if (isDateRange(selectedValue)) {
                                            // add range modifiers
                                            const { from, to } = selectedValue;
                                            modifiers[SelectionState.range_start] = Boolean(from && to && dateLib.isSameDay(date, from));
                                            modifiers[SelectionState.range_end] = Boolean(from && to && dateLib.isSameDay(date, to));
                                            modifiers[SelectionState.range_middle] =
                                                rangeIncludesDate(selectedValue, date, true, dateLib);
                                        }
                                        const style = getStyleForModifiers(modifiers, styles, props.modifiersStyles);
                                        const className = getClassNamesForModifiers(modifiers, classNames, props.modifiersClassNames);
                                        const ariaLabel = !isInteractive && !modifiers.hidden
                                            ? labelGridcell(date, modifiers, dateLib.options, dateLib)
                                            : undefined;
                                        return (React3.createElement(components.Day, { key: `${day.isoDate}_${day.displayMonthId}`, day: day, modifiers: modifiers, className: className.join(" "), style: style, role: "gridcell", "aria-selected": modifiers.selected || undefined, "aria-label": ariaLabel, "data-day": day.isoDate, "data-month": day.outside ? day.dateMonthId : undefined, "data-selected": modifiers.selected || undefined, "data-disabled": modifiers.disabled || undefined, "data-hidden": modifiers.hidden || undefined, "data-outside": day.outside || undefined, "data-focused": modifiers.focused || undefined, "data-today": modifiers.today || undefined }, !modifiers.hidden && isInteractive ? (React3.createElement(components.DayButton, { className: classNames[UI.DayButton], style: styles?.[UI.DayButton], type: "button", day: day, modifiers: modifiers, disabled: (!modifiers.focused &&
                                                modifiers.disabled) ||
                                                undefined, "aria-disabled": (modifiers.focused &&
                                                modifiers.disabled) ||
                                                undefined, tabIndex: isFocusTarget(day) ? 0 : -1, "aria-label": labelDayButton(date, modifiers, dateLib.options, dateLib), onClick: handleDayClick(day, modifiers), onBlur: handleDayBlur(day, modifiers), onFocus: handleDayFocus(day, modifiers), onKeyDown: handleDayKeyDown(day, modifiers), onMouseEnter: handleDayMouseEnter(day, modifiers), onMouseLeave: handleDayMouseLeave(day, modifiers) }, formatDay(date, dateLib.options, dateLib))) : (!modifiers.hidden &&
                                            formatDay(day.date, dateLib.options, dateLib))));
                                    })));
                            })))));
                })),
            props.footer && (React3.createElement(components.Footer, { className: classNames[UI.Footer], style: styles?.[UI.Footer], role: "status", "aria-live": "polite" }, props.footer)))));
}

function Calendar({
  className,
  classNames,
  showOutsideDays = true,
  captionLayout = "label",
  buttonVariant = "ghost",
  formatters,
  components,
  ...props
}) {
  const defaultClassNames = getDefaultClassNames();
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    DayPicker,
    {
      showOutsideDays,
      className: cn(
        "bg-background max-w-full group/calendar p-3 [--cell-size:--spacing(8)] [[data-slot=card-content]_&]:bg-transparent [[data-slot=popover-content]_&]:bg-transparent",
        String.raw`rtl:**:[.rdp-button\_next>svg]:rotate-180`,
        String.raw`rtl:**:[.rdp-button\_previous>svg]:rotate-180`,
        className
      ),
      captionLayout,
      formatters: {
        formatMonthDropdown: (date) => date.toLocaleString("default", { month: "short" }),
        ...formatters
      },
      classNames: {
        root: cn("w-fit", defaultClassNames.root),
        months: cn(
          "flex gap-4 flex-col md:flex-row relative",
          defaultClassNames.months
        ),
        month: cn("flex flex-col w-full gap-4", defaultClassNames.month),
        nav: cn(
          "flex items-center gap-1 w-full absolute top-0 inset-x-0 justify-between",
          defaultClassNames.nav
        ),
        button_previous: cn(
          variants({ variant: buttonVariant }),
          "size-(--cell-size) aria-disabled:opacity-50 p-0 select-none",
          defaultClassNames.button_previous
        ),
        button_next: cn(
          variants({ variant: buttonVariant }),
          "size-(--cell-size) aria-disabled:opacity-50 p-0 select-none",
          defaultClassNames.button_next
        ),
        month_caption: cn(
          "flex center h-(--cell-size) w-full px-(--cell-size)",
          defaultClassNames.month_caption
        ),
        dropdowns: cn(
          "w-full flex items-center text-sm font-medium justify-center h-(--cell-size) gap-1.5",
          defaultClassNames.dropdowns
        ),
        dropdown_root: cn(
          "relative has-focus:border-ring border border-input shadow-xs has-focus:ring-ring/50 has-focus:ring-[3px] rounded",
          defaultClassNames.dropdown_root
        ),
        dropdown: cn(
          "absolute bg-popover inset-0 opacity-0",
          defaultClassNames.dropdown
        ),
        caption_label: cn(
          "select-none font-medium",
          captionLayout === "label" ? "text-sm" : "rounded pl-2 pr-1 flex items-center gap-1 text-sm h-8 [&>svg]:text-muted-foreground [&>svg]:size-3.5",
          defaultClassNames.caption_label
        ),
        table: "w-full border-collapse",
        weekdays: cn("flex", defaultClassNames.weekdays),
        weekday: cn(
          "text-muted-foreground rounded flex-1 font-normal text-[0.8rem] select-none",
          defaultClassNames.weekday
        ),
        week: cn("flex w-full mt-2", defaultClassNames.week),
        week_number_header: cn(
          "select-none w-(--cell-size)",
          defaultClassNames.week_number_header
        ),
        week_number: cn(
          "text-[0.8rem] select-none text-muted-foreground",
          defaultClassNames.week_number
        ),
        day: cn(
          "relative w-full h-full p-0 text-center [&:first-child[data-selected=true]_button]:rounded-l [&:last-child[data-selected=true]_button]:rounded-r group/day aspect-square select-none",
          defaultClassNames.day
        ),
        range_start: cn("rounded-l bg-accent", defaultClassNames.range_start),
        range_middle: cn("rounded-none", defaultClassNames.range_middle),
        range_end: cn("rounded-r bg-accent", defaultClassNames.range_end),
        today: cn(
          "bg-accent text-accent-foreground rounded data-[selected=true]:rounded-none",
          defaultClassNames.today
        ),
        outside: cn(
          "text-muted-foreground aria-selected:text-muted-foreground",
          defaultClassNames.outside
        ),
        disabled: cn(
          "text-muted-foreground opacity-50",
          defaultClassNames.disabled
        ),
        hidden: cn("invisible", defaultClassNames.hidden),
        ...classNames
      },
      components: {
        Root: ({ className: className2, rootRef, ...props2 }) => {
          return /* @__PURE__ */ jsxRuntimeExports.jsx(
            "div",
            {
              "data-slot": "calendar",
              ref: rootRef,
              className: cn(className2),
              ...props2
            }
          );
        },
        Chevron: ({ className: className2, orientation, ...props2 }) => {
          if (orientation === "left") {
            return /* @__PURE__ */ jsxRuntimeExports.jsx(ChevronLeft, { className: cn("size-4", className2), ...props2 });
          }
          if (orientation === "right") {
            return /* @__PURE__ */ jsxRuntimeExports.jsx(
              ChevronRight,
              {
                className: cn("size-4", className2),
                ...props2
              }
            );
          }
          return /* @__PURE__ */ jsxRuntimeExports.jsx(ChevronDown, { className: cn("size-4", className2), ...props2 });
        },
        DayButton: CalendarDayButton,
        WeekNumber: ({ children, ...props2 }) => {
          return /* @__PURE__ */ jsxRuntimeExports.jsx("td", { ...props2, children: /* @__PURE__ */ jsxRuntimeExports.jsx("div", { className: "flex size-(--cell-size) center text-center", children }) });
        },
        ...components
      },
      ...props
    }
  );
}
function CalendarDayButton({
  className,
  day,
  modifiers,
  ...props
}) {
  const defaultClassNames = getDefaultClassNames();
  const ref = __mf_37(null);
  __mf_28(() => {
    if (modifiers.focused) ref.current?.focus();
  }, [modifiers.focused]);
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Button$1,
    {
      ref,
      variant: "ghost",
      "data-day": day.date.toLocaleDateString(),
      "data-slot": "calendar-day-button",
      "data-selected-single": modifiers.selected && !modifiers.range_start && !modifiers.range_end && !modifiers.range_middle,
      "data-range-start": modifiers.range_start,
      "data-range-end": modifiers.range_end,
      "data-range-middle": modifiers.range_middle,
      className: cn(
        "h-9 px-4 py-2 has-[>svg]:px-3 rounded",
        "data-[selected-single=true]:bg-primary data-[selected-single=true]:text-primary-foreground data-[range-middle=true]:bg-primary/15 data-[range-middle=true]:text-muted-foreground data-[range-start=true]:bg-primary data-[range-start=true]:text-primary-foreground data-[range-end=true]:bg-primary data-[range-end=true]:text-primary-foreground group-data-[focused=true]/day:border-ring group-data-[focused=true]/day:ring-ring/50 dark:hover:text-accent-foreground flex aspect-square size-auto w-full min-w-(--cell-size) flex-col gap-1 leading-none font-normal group-data-[focused=true]/day:relative group-data-[focused=true]/day:z-10 group-data-[focused=true]/day:ring-[3px] data-[range-end=true]:rounded data-[range-end=true]:rounded-r data-[range-middle=true]:rounded-none data-[range-start=true]:rounded data-[range-start=true]:rounded-l [&>span]:text-xs [&>span]:opacity-70",
        defaultClassNames.day,
        className
      ),
      ...props
    }
  );
}

function Chip({
  label,
  leading,
  trailing,
  className,
  active,
  variant = "outlined",
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    Button$1,
    {
      "data-slot": "chip",
      "data-variant": variant,
      type: "button",
      ...props,
      variant: active ? "filled" : variant,
      className: cn(
        "px-2.5 h-max not-disabled:hover:shadow [&_button]:size-5 rounded-full",
        className
      ),
      children: [
        leading && leading,
        label,
        trailing && trailing
      ]
    }
  );
}

var U=1,Y$1=.9,H=.8,J=.17,p=.1,u=.999,$=.9999;var k$1=.99,m=/[\\\/_+.#"@\[\(\{&]/,B$1=/[\\\/_+.#"@\[\(\{&]/g,K$1=/[\s-]/,X=/[\s-]/g;function G(_,C,h,P,A,f,O){if(f===C.length)return A===_.length?U:k$1;var T=`${A},${f}`;if(O[T]!==void 0)return O[T];for(var L=P.charAt(f),c=h.indexOf(L,A),S=0,E,N,R,M;c>=0;)E=G(_,C,h,P,c+1,f+1,O),E>S&&(c===A?E*=U:m.test(_.charAt(c-1))?(E*=H,R=_.slice(A,c-1).match(B$1),R&&A>0&&(E*=Math.pow(u,R.length))):K$1.test(_.charAt(c-1))?(E*=Y$1,M=_.slice(A,c-1).match(X),M&&A>0&&(E*=Math.pow(u,M.length))):(E*=J,A>0&&(E*=Math.pow(u,c-A))),_.charAt(c)!==C.charAt(f)&&(E*=$)),(E<p&&h.charAt(c-1)===P.charAt(f+1)||P.charAt(f+1)===P.charAt(f)&&h.charAt(c-1)!==P.charAt(f))&&(N=G(_,C,h,P,c+1,f+2,O),N*p>E&&(E=N*p)),E>S&&(S=E),c=h.indexOf(L,c+1);return O[T]=S,S}function D(_){return _.toLowerCase().replace(X," ")}function W(_,C,h){return _=h&&h.length>0?`${_+" "+h.join(" ")}`:_,G(_,C,D(_),D(C),0,0,{})}

// src/primitive.tsx
var NODES$1 = [
  "a",
  "button",
  "div",
  "form",
  "h2",
  "h3",
  "img",
  "input",
  "label",
  "li",
  "nav",
  "ol",
  "p",
  "select",
  "span",
  "svg",
  "ul"
];
var Primitive$1 = NODES$1.reduce((primitive, node) => {
  const Slot = createSlot$1(`Primitive.${node}`);
  const Node = __mf_16((props, forwardedRef) => {
    const { asChild, ...primitiveProps } = props;
    const Comp = asChild ? Slot : node;
    if (typeof window !== "undefined") {
      window[Symbol.for("radix-ui")] = true;
    }
    return /* @__PURE__ */ jsxRuntimeExports.jsx(Comp, { ...primitiveProps, ref: forwardedRef });
  });
  Node.displayName = `Primitive.${node}`;
  return { ...primitive, [node]: Node };
}, {});

var N='[cmdk-group=""]',Y='[cmdk-group-items=""]',be='[cmdk-group-heading=""]',le='[cmdk-item=""]',ce=`${le}:not([aria-disabled="true"])`,Z="cmdk-item-select",T="data-value",Re=(r,o,n)=>W(r,o,n),ue=__mf_13(void 0),K=()=>__mf_25(ue),de=__mf_13(void 0),ee=()=>__mf_25(de),fe=__mf_13(void 0),me=__mf_16((r,o)=>{let n=L(()=>{var e,a;return {search:"",value:(a=(e=r.value)!=null?e:r.defaultValue)!=null?a:"",selectedItemId:void 0,filtered:{count:0,items:new Map,groups:new Set}}}),u=L(()=>new Set),c=L(()=>new Map),d=L(()=>new Map),f=L(()=>new Set),p=pe(r),{label:b,children:m,value:R,onValueChange:x,filter:C,shouldFilter:S,loop:A,disablePointerSelection:ge=false,vimBindings:j=true,...O}=r,$=useId(),q=useId(),_=useId(),I=__mf_37(null),v=ke();k(()=>{if(R!==void 0){let e=R.trim();n.current.value=e,E.emit();}},[R]),k(()=>{v(6,ne);},[]);let E=__mf_34(()=>({subscribe:e=>(f.current.add(e),()=>f.current.delete(e)),snapshot:()=>n.current,setState:(e,a,s)=>{var i,l,g,y;if(!Object.is(n.current[e],a)){if(n.current[e]=a,e==="search")J(),z(),v(1,W);else if(e==="value"){if(document.activeElement.hasAttribute("cmdk-input")||document.activeElement.hasAttribute("cmdk-root")){let h=document.getElementById(_);h?h.focus():(i=document.getElementById($))==null||i.focus();}if(v(7,()=>{var h;n.current.selectedItemId=(h=M())==null?void 0:h.id,E.emit();}),s||v(5,ne),((l=p.current)==null?void 0:l.value)!==void 0){let h=a!=null?a:"";(y=(g=p.current).onValueChange)==null||y.call(g,h);return}}E.emit();}},emit:()=>{f.current.forEach(e=>e());}}),[]),U=__mf_34(()=>({value:(e,a,s)=>{var i;a!==((i=d.current.get(e))==null?void 0:i.value)&&(d.current.set(e,{value:a,keywords:s}),n.current.filtered.items.set(e,te(a,s)),v(2,()=>{z(),E.emit();}));},item:(e,a)=>(u.current.add(e),a&&(c.current.has(a)?c.current.get(a).add(e):c.current.set(a,new Set([e]))),v(3,()=>{J(),z(),n.current.value||W(),E.emit();}),()=>{d.current.delete(e),u.current.delete(e),n.current.filtered.items.delete(e);let s=M();v(4,()=>{J(),(s==null?void 0:s.getAttribute("id"))===e&&W(),E.emit();});}),group:e=>(c.current.has(e)||c.current.set(e,new Set),()=>{d.current.delete(e),c.current.delete(e);}),filter:()=>p.current.shouldFilter,label:b||r["aria-label"],getDisablePointerSelection:()=>p.current.disablePointerSelection,listId:$,inputId:_,labelId:q,listInnerRef:I}),[]);function te(e,a){var i,l;let s=(l=(i=p.current)==null?void 0:i.filter)!=null?l:Re;return e?s(e,n.current.search,a):0}function z(){if(!n.current.search||p.current.shouldFilter===false)return;let e=n.current.filtered.items,a=[];n.current.filtered.groups.forEach(i=>{let l=c.current.get(i),g=0;l.forEach(y=>{let h=e.get(y);g=Math.max(h,g);}),a.push([i,g]);});let s=I.current;V().sort((i,l)=>{var h,F;let g=i.getAttribute("id"),y=l.getAttribute("id");return ((h=e.get(y))!=null?h:0)-((F=e.get(g))!=null?F:0)}).forEach(i=>{let l=i.closest(Y);l?l.appendChild(i.parentElement===l?i:i.closest(`${Y} > *`)):s.appendChild(i.parentElement===s?i:i.closest(`${Y} > *`));}),a.sort((i,l)=>l[1]-i[1]).forEach(i=>{var g;let l=(g=I.current)==null?void 0:g.querySelector(`${N}[${T}="${encodeURIComponent(i[0])}"]`);l==null||l.parentElement.appendChild(l);});}function W(){let e=V().find(s=>s.getAttribute("aria-disabled")!=="true"),a=e==null?void 0:e.getAttribute(T);E.setState("value",a||void 0);}function J(){var a,s,i,l;if(!n.current.search||p.current.shouldFilter===false){n.current.filtered.count=u.current.size;return}n.current.filtered.groups=new Set;let e=0;for(let g of u.current){let y=(s=(a=d.current.get(g))==null?void 0:a.value)!=null?s:"",h=(l=(i=d.current.get(g))==null?void 0:i.keywords)!=null?l:[],F=te(y,h);n.current.filtered.items.set(g,F),F>0&&e++;}for(let[g,y]of c.current)for(let h of y)if(n.current.filtered.items.get(h)>0){n.current.filtered.groups.add(g);break}n.current.filtered.count=e;}function ne(){var a,s,i;let e=M();e&&(((a=e.parentElement)==null?void 0:a.firstChild)===e&&((i=(s=e.closest(N))==null?void 0:s.querySelector(be))==null||i.scrollIntoView({block:"nearest"})),e.scrollIntoView({block:"nearest"}));}function M(){var e;return (e=I.current)==null?void 0:e.querySelector(`${le}[aria-selected="true"]`)}function V(){var e;return Array.from(((e=I.current)==null?void 0:e.querySelectorAll(ce))||[])}function X(e){let s=V()[e];s&&E.setState("value",s.getAttribute(T));}function Q(e){var g;let a=M(),s=V(),i=s.findIndex(y=>y===a),l=s[i+e];(g=p.current)!=null&&g.loop&&(l=i+e<0?s[s.length-1]:i+e===s.length?s[0]:s[i+e]),l&&E.setState("value",l.getAttribute(T));}function re(e){let a=M(),s=a==null?void 0:a.closest(N),i;for(;s&&!i;)s=e>0?we(s,N):De(s,N),i=s==null?void 0:s.querySelector(ce);i?E.setState("value",i.getAttribute(T)):Q(e);}let oe=()=>X(V().length-1),ie=e=>{e.preventDefault(),e.metaKey?oe():e.altKey?re(1):Q(1);},se=e=>{e.preventDefault(),e.metaKey?X(0):e.altKey?re(-1):Q(-1);};return __mf_14(Primitive$1.div,{ref:o,tabIndex:-1,...O,"cmdk-root":"",onKeyDown:e=>{var s;(s=O.onKeyDown)==null||s.call(O,e);let a=e.nativeEvent.isComposing||e.keyCode===229;if(!(e.defaultPrevented||a))switch(e.key){case "n":case "j":{j&&e.ctrlKey&&ie(e);break}case "ArrowDown":{ie(e);break}case "p":case "k":{j&&e.ctrlKey&&se(e);break}case "ArrowUp":{se(e);break}case "Home":{e.preventDefault(),X(0);break}case "End":{e.preventDefault(),oe();break}case "Enter":{e.preventDefault();let i=M();if(i){let l=new Event(Z);i.dispatchEvent(l);}}}}},__mf_14("label",{"cmdk-label":"",htmlFor:U.inputId,id:U.labelId,style:Te},b),B(r,e=>__mf_14(de.Provider,{value:E},__mf_14(ue.Provider,{value:U},e))))}),he=__mf_16((r,o)=>{var _,I;let n=useId(),u=__mf_37(null),c=__mf_25(fe),d=K(),f=pe(r),p=(I=(_=f.current)==null?void 0:_.forceMount)!=null?I:c==null?void 0:c.forceMount;k(()=>{if(!p)return d.item(n,c==null?void 0:c.id)},[p]);let b=ve(n,u,[r.value,r.children,u],r.keywords),m=ee(),R=P(v=>v.value&&v.value===b.current),x=P(v=>p||d.filter()===false?true:v.search?v.filtered.items.get(n)>0:true);__mf_28(()=>{let v=u.current;if(!(!v||r.disabled))return v.addEventListener(Z,C),()=>v.removeEventListener(Z,C)},[x,r.onSelect,r.disabled]);function C(){var v,E;S(),(E=(v=f.current).onSelect)==null||E.call(v,b.current);}function S(){m.setState("value",b.current,true);}if(!x)return null;let{disabled:A,value:ge,onSelect:j,forceMount:O,keywords:$,...q}=r;return __mf_14(Primitive$1.div,{ref:composeRefs(u,o),...q,id:n,"cmdk-item":"",role:"option","aria-disabled":!!A,"aria-selected":!!R,"data-disabled":!!A,"data-selected":!!R,onPointerMove:A||d.getDisablePointerSelection()?void 0:S,onClick:A?void 0:C},r.children)}),Ee=__mf_16((r,o)=>{let{heading:n,children:u,forceMount:c,...d}=r,f=useId(),p=__mf_37(null),b=__mf_37(null),m=useId(),R=K(),x=P(S=>c||R.filter()===false?true:S.search?S.filtered.groups.has(f):true);k(()=>R.group(f),[]),ve(f,p,[r.value,r.heading,b]);let C=__mf_34(()=>({id:f,forceMount:c}),[c]);return __mf_14(Primitive$1.div,{ref:composeRefs(p,o),...d,"cmdk-group":"",role:"presentation",hidden:x?void 0:true},n&&__mf_14("div",{ref:b,"cmdk-group-heading":"","aria-hidden":true,id:m},n),B(r,S=>__mf_14("div",{"cmdk-group-items":"",role:"group","aria-labelledby":n?m:void 0},__mf_14(fe.Provider,{value:C},S))))}),ye=__mf_16((r,o)=>{let{alwaysRender:n,...u}=r,c=__mf_37(null),d=P(f=>!f.search);return !n&&!d?null:__mf_14(Primitive$1.div,{ref:composeRefs(c,o),...u,"cmdk-separator":"",role:"separator"})}),Se=__mf_16((r,o)=>{let{onValueChange:n,...u}=r,c=r.value!=null,d=ee(),f=P(m=>m.search),p=P(m=>m.selectedItemId),b=K();return __mf_28(()=>{r.value!=null&&d.setState("search",r.value);},[r.value]),__mf_14(Primitive$1.input,{ref:o,...u,"cmdk-input":"",autoComplete:"off",autoCorrect:"off",spellCheck:false,"aria-autocomplete":"list",role:"combobox","aria-expanded":true,"aria-controls":b.listId,"aria-labelledby":b.labelId,"aria-activedescendant":p,id:b.inputId,type:"text",value:c?r.value:f,onChange:m=>{c||d.setState("search",m.target.value),n==null||n(m.target.value);}})}),Ce=__mf_16((r,o)=>{let{children:n,label:u="Suggestions",...c}=r,d=__mf_37(null),f=__mf_37(null),p=P(m=>m.selectedItemId),b=K();return __mf_28(()=>{if(f.current&&d.current){let m=f.current,R=d.current,x,C=new ResizeObserver(()=>{x=requestAnimationFrame(()=>{let S=m.offsetHeight;R.style.setProperty("--cmdk-list-height",S.toFixed(1)+"px");});});return C.observe(m),()=>{cancelAnimationFrame(x),C.unobserve(m);}}},[]),__mf_14(Primitive$1.div,{ref:composeRefs(d,o),...c,"cmdk-list":"",role:"listbox",tabIndex:-1,"aria-activedescendant":p,"aria-label":u,id:b.listId},B(r,m=>__mf_14("div",{ref:composeRefs(f,b.listInnerRef),"cmdk-list-sizer":""},m)))}),xe=__mf_16((r,o)=>{let{open:n,onOpenChange:u,overlayClassName:c,contentClassName:d,container:f,...p}=r;return __mf_14(Root$1,{open:n,onOpenChange:u},__mf_14(Portal$1,{container:f},__mf_14(Overlay,{"cmdk-overlay":"",className:c}),__mf_14(Content,{"aria-label":r.label,"cmdk-dialog":"",className:d},__mf_14(me,{ref:o,...p}))))}),Ie=__mf_16((r,o)=>P(u=>u.filtered.count===0)?__mf_14(Primitive$1.div,{ref:o,...r,"cmdk-empty":"",role:"presentation"}):null),Pe=__mf_16((r,o)=>{let{progress:n,children:u,label:c="Loading...",...d}=r;return __mf_14(Primitive$1.div,{ref:o,...d,"cmdk-loading":"",role:"progressbar","aria-valuenow":n,"aria-valuemin":0,"aria-valuemax":100,"aria-label":c},B(r,f=>__mf_14("div",{"aria-hidden":true},f)))}),_e=Object.assign(me,{List:Ce,Item:he,Input:Se,Group:Ee,Separator:ye,Dialog:xe,Empty:Ie,Loading:Pe});function we(r,o){let n=r.nextElementSibling;for(;n;){if(n.matches(o))return n;n=n.nextElementSibling;}}function De(r,o){let n=r.previousElementSibling;for(;n;){if(n.matches(o))return n;n=n.previousElementSibling;}}function pe(r){let o=__mf_37(r);return k(()=>{o.current=r;}),o}var k=typeof window=="undefined"?__mf_28:__mf_33;function L(r){let o=__mf_37();return o.current===void 0&&(o.current=r()),o}function P(r){let o=ee(),n=()=>r(o.snapshot());return __mf_39(o.subscribe,n,n)}function ve(r,o,n,u=[]){let c=__mf_37(),d=K();return k(()=>{var b;let f=(()=>{var m;for(let R of n){if(typeof R=="string")return R.trim();if(typeof R=="object"&&"current"in R)return R.current?(m=R.current.textContent)==null?void 0:m.trim():c.current}})(),p=u.map(m=>m.trim());d.value(r,f,p),(b=o.current)==null||b.setAttribute(T,f),c.current=f;}),c}var ke=()=>{let[r,o]=__mf_38(),n=L(()=>new Map);return k(()=>{n.current.forEach(u=>u()),n.current=new Map;},[r]),(u,c)=>{n.current.set(u,c),o({});}};function Me(r){let o=r.type;return typeof o=="function"?o(r.props):"render"in o?o.render(r.props):r}function B({asChild:r,children:o},n){return r&&__mf_17(o)?__mf_12(Me(o),{ref:o.ref},n(o.props.children)):n(o)}var Te={position:"absolute",width:"1px",height:"1px",padding:"0",margin:"-1px",overflow:"hidden",clip:"rect(0, 0, 0, 0)",whiteSpace:"nowrap",borderWidth:"0"};

function Command({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e,
    {
      "data-slot": "command",
      className: cn(
        "bg-popover text-popover-foreground flex h-full w-full flex-col overflow-hidden rounded-md",
        className
      ),
      ...props
    }
  );
}
function CommandDialog({
  title = "Command Palette",
  description = "Search for a command to run...",
  children,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Dialog, { ...props, children: [
    /* @__PURE__ */ jsxRuntimeExports.jsxs(DialogHeader, { className: "sr-only", children: [
      /* @__PURE__ */ jsxRuntimeExports.jsx(DialogTitle, { children: title }),
      /* @__PURE__ */ jsxRuntimeExports.jsx(DialogDescription, { children: description })
    ] }),
    /* @__PURE__ */ jsxRuntimeExports.jsx(DialogContent, { className: "overflow-hidden p-0", children: /* @__PURE__ */ jsxRuntimeExports.jsx(Command, { className: "[&_[cmdk-group-heading]]:text-muted-foreground **:data-[slot=command-input-wrapper]:h-12 [&_[cmdk-group-heading]]:px-2 [&_[cmdk-group-heading]]:font-medium [&_[cmdk-group]]:px-2 [&_[cmdk-group]:not([hidden])_~[cmdk-group]]:pt-0 [&_[cmdk-input-wrapper]_svg]:h-5 [&_[cmdk-input-wrapper]_svg]:w-5 [&_[cmdk-input]]:h-12 [&_[cmdk-item]]:px-2 [&_[cmdk-item]]:py-3 [&_[cmdk-item]_svg]:h-5 [&_[cmdk-item]_svg]:w-5", children }) })
  ] });
}
function CommandInput({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      "data-slot": "command-input-wrapper",
      className: "flex h-9 items-center gap-2 border-b px-3",
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Search, { className: "size-4 shrink-0 opacity-50" }),
        /* @__PURE__ */ jsxRuntimeExports.jsx(
          _e.Input,
          {
            "data-slot": "command-input",
            className: cn(
              "placeholder:text-muted-foreground flex h-10 w-full rounded-md bg-transparent py-3 text-sm outline-hidden disabled:cursor-not-allowed disabled:opacity-50",
              className
            ),
            ...props
          }
        )
      ]
    }
  );
}
function CommandList({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e.List,
    {
      "data-slot": "command-list",
      className: cn(
        "max-h-[300px] scroll-py-1 overflow-x-hidden overflow-y-auto",
        className
      ),
      ...props
    }
  );
}
function CommandEmpty({
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e.Empty,
    {
      "data-slot": "command-empty",
      className: "py-6 text-center text-sm",
      ...props
    }
  );
}
function CommandGroup({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e.Group,
    {
      "data-slot": "command-group",
      className: cn(
        "[&_[cmdk-group-items]]:flex [&_[cmdk-group-items]]:flex-col [&_[cmdk-group-items]]:gap-0.5",
        "text-foreground [&_[cmdk-group-heading]]:text-muted-foreground overflow-hidden p-1 [&_[cmdk-group-heading]]:px-2 [&_[cmdk-group-heading]]:py-1.5 [&_[cmdk-group-heading]]:text-xs [&_[cmdk-group-heading]]:font-medium",
        className
      ),
      ...props
    }
  );
}
function CommandSeparator({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e.Separator,
    {
      "data-slot": "command-separator",
      className: cn("bg-border -mx-1 h-px", className),
      ...props
    }
  );
}
function CommandItem({
  className,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    _e.Item,
    {
      "data-slot": "command-item",
      className: cn(
        "hover:bg-primary/25 hover:text-accent-foreground [&_svg:not([class*='text-'])]:text-muted-foreground relative flex cursor-default items-center gap-2 rounded-sm px-2 py-1.5 text-sm outline-hidden select-none data-[disabled=true]:pointer-events-none data-[disabled=true]:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        className
      ),
      ...props
    }
  );
}
function CommandShortcut({ className, ...props }) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    "span",
    {
      "data-slot": "command-shortcut",
      className: cn(
        "text-muted-foreground ml-auto text-xs tracking-widest",
        className
      ),
      ...props
    }
  );
}

function Autocomplete({
  value,
  label,
  helperText,
  error,
  onValueChange,
  empty,
  options,
  getOptionValue = (option) => option.value,
  getOptionLabel = (option) => option.label,
  placeholder,
  searchPlaceholder,
  className,
  freeSolo,
  freeSoloLabel,
  name,
  disabled,
  mode,
  renderOption,
  renderSelectedLeading,
  ...props
}) {
  const [open, setOpen] = __mf_38(false);
  const isDesktop = useMedia("(min-width: 768px)");
  const fullOptions = __mf_34(() => {
    if (!freeSolo) {
      return options;
    }
    if (mode === "multiple") {
      return [
        ...options,
        ...value.filter(
          (item) => !options.some((option) => getOptionValue(option) === item)
        ).map((item) => ({ value: item, label: item }))
      ];
    }
    return [
      ...options,
      ...!options.some((option) => getOptionValue(option) === value) && value ? [{ value, label: value }] : []
    ];
  }, [freeSolo, options, value, mode, getOptionValue]);
  const handleRemove = __mf_24(
    (target) => {
      if (mode === "multiple") {
        onValueChange?.(value.filter((item) => item !== target));
      }
      if (mode === "single") {
        onValueChange?.("");
      }
    },
    [mode, onValueChange, value]
  );
  const handleUpdate = __mf_24(
    (target) => {
      if (mode === "multiple") {
        if (value.includes(target)) {
          onValueChange?.(value.filter((item) => item !== target));
        } else {
          onValueChange?.([...value, target]);
        }
      }
      if (mode === "single") {
        if (value === target) {
          onValueChange?.("");
        } else {
          onValueChange?.(target);
        }
      }
      setOpen(false);
    },
    [mode, onValueChange, value]
  );
  if (isDesktop) {
    return /* @__PURE__ */ jsxRuntimeExports.jsxs(Popover, { open, onOpenChange: setOpen, children: [
      /* @__PURE__ */ jsxRuntimeExports.jsx(PopoverTrigger, { name, disabled, asChild: true, children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        AutocompleteTrigger,
        {
          ...props,
          open,
          mode,
          value,
          label,
          helperText,
          placeholder: placeholder || label,
          options: fullOptions,
          error,
          className,
          onRemove: handleRemove,
          getOptionValue,
          getOptionLabel,
          renderSelectedLeading
        }
      ) }),
      /* @__PURE__ */ jsxRuntimeExports.jsx(PopoverContent, { children: /* @__PURE__ */ jsxRuntimeExports.jsx(
        AutocompleteItemList,
        {
          name,
          mode,
          options: fullOptions,
          value,
          empty,
          onSelect: handleUpdate,
          freeSolo,
          freeSoloLabel,
          renderOption,
          getOptionValue,
          getOptionLabel
        }
      ) })
    ] });
  }
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Dialog, { open, onOpenChange: setOpen, children: [
    /* @__PURE__ */ jsxRuntimeExports.jsx(DialogTrigger, { name, disabled, asChild: true, children: /* @__PURE__ */ jsxRuntimeExports.jsx(
      AutocompleteTrigger,
      {
        ...props,
        open,
        mode,
        value,
        label,
        helperText,
        placeholder: placeholder || label,
        options: fullOptions,
        error,
        className,
        onRemove: handleRemove,
        getOptionValue,
        getOptionLabel,
        renderSelectedLeading
      }
    ) }),
    /* @__PURE__ */ jsxRuntimeExports.jsx(DialogContent, { children: /* @__PURE__ */ jsxRuntimeExports.jsx(
      AutocompleteItemList,
      {
        name,
        mode,
        options: fullOptions,
        value,
        empty,
        onSelect: handleUpdate,
        freeSolo,
        freeSoloLabel,
        renderOption,
        getOptionValue,
        getOptionLabel
      }
    ) })
  ] });
}
function AutocompleteItemList({
  name,
  empty,
  value,
  options,
  searchPlaceholder,
  onSelect,
  freeSolo,
  freeSoloLabel,
  renderOption,
  getOptionValue = (option) => option.value,
  getOptionLabel = (option) => option.label
}) {
  const [query, setQuery] = __mf_38("");
  const showAddButton = __mf_34(
    () => freeSolo && query && !options.some(
      (option) => getOptionLabel(option)?.toLowerCase() === query.toLowerCase()
    ),
    [freeSolo, query, options, getOptionLabel]
  );
  const handleSelect = __mf_24(
    (selectedValue) => () => {
      onSelect(selectedValue);
    },
    [onSelect]
  );
  const handleKeyUp = __mf_24(
    (event) => {
      if (event.key === "Enter" && freeSolo && query) {
        event.preventDefault();
        onSelect(query);
      }
    },
    [freeSolo, query, onSelect]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Command, { children: [
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      CommandInput,
      {
        value: query,
        placeholder: searchPlaceholder ?? "Search ...",
        className: "h-9",
        onValueChange: setQuery,
        onKeyUp: handleKeyUp
      }
    ),
    /* @__PURE__ */ jsxRuntimeExports.jsxs(CommandList, { children: [
      /* @__PURE__ */ jsxRuntimeExports.jsxs(CommandEmpty, { children: [
        showAddButton && /* @__PURE__ */ jsxRuntimeExports.jsx(Button$1, { variant: "tonal", onClick: handleSelect(query), children: freeSoloLabel?.(query) ?? `Add "${query}"` }),
        !freeSolo && !options.some(
          (option) => getOptionLabel(option).toLowerCase().includes(query.toLowerCase())
        ) && (empty || "No option found.")
      ] }),
      /* @__PURE__ */ jsxRuntimeExports.jsx(CommandGroup, { children: options.map((option, index) => /* @__PURE__ */ jsxRuntimeExports.jsx(
        CommandItem,
        {
          value: getOptionLabel(option),
          onSelect: () => {
            onSelect(getOptionValue(option));
          },
          className: cn("border-l-2 border-transparent", {
            "rounded-l-none bg-primary/5 border-primary/60": value?.includes(getOptionValue(option))
          }),
          asChild: !!renderOption,
          "aria-selected": !!value?.includes(getOptionValue(option)),
          "data-cy": `${name ?? "option"}-${index}`,
          children: renderOption ? renderOption(option) : getOptionLabel(option)
        },
        getOptionValue(option)
      )) })
    ] })
  ] });
}
function AutocompleteTrigger({
  value,
  label,
  helperText,
  error,
  onValueChange,
  empty,
  options,
  getOptionValue = (option) => option.value,
  getOptionLabel = (option) => option.label,
  placeholder,
  className,
  onRemove,
  open,
  mode,
  renderSelectedLeading,
  ...props
}) {
  const handleRemove = __mf_24(
    (item) => (event) => {
      event.stopPropagation();
      onRemove(item);
    },
    [onRemove]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      "data-slot": "autocomplete-field",
      className: cn("grid w-full gap-1.5", className),
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Label, { children: label }),
        /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { children: [
          /* @__PURE__ */ jsxRuntimeExports.jsxs(
            "div",
            {
              ...props,
              className: cn(
                "flex gap-2 p-1 flex-wrap min-h-12 items-center w-full min-w-[280px] rounded-sm border transition-all has-disabled:pointer-events-none has-disabled:cursor-not-allowed",
                "focus-within:border-primary focus-within:ring-[3px] focus-within:ring-primary/30",
                "hover:border-primary hover:ring-[2px] hover:ring-primary/25",
                "[&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4",
                "bg-muted/5 has-disabled:bg-transparent",
                "cursor-pointer disabled:cursor-not-allowed",
                {
                  "border-primary ring-[3px] ring-primary/30": open,
                  "bg-primary/10": !!value?.length,
                  "border-error": error
                }
              ),
              children: [
                mode === "multiple" && Array.isArray(value) ? value.map((item, index) => {
                  const option = options.find(
                    (option2) => getOptionValue(option2) === item
                  );
                  return /* @__PURE__ */ jsxRuntimeExports.jsx(
                    Chip,
                    {
                      label: option ? getOptionLabel(option) : item,
                      leading: (option ? renderSelectedLeading?.(option) : void 0) ?? "",
                      trailing: /* @__PURE__ */ jsxRuntimeExports.jsx(
                        "button",
                        {
                          type: "button",
                          onClick: handleRemove(item),
                          className: cn(variants$1(), "size-5"),
                          children: /* @__PURE__ */ jsxRuntimeExports.jsx(X$1, {})
                        }
                      )
                    },
                    `item-${index.toString()}`
                  );
                }) : !!value?.length && (() => {
                  const option = options.find(
                    (option2) => getOptionValue(option2) === value
                  );
                  return /* @__PURE__ */ jsxRuntimeExports.jsx(
                    Chip,
                    {
                      label: option ? getOptionLabel(option) : value,
                      leading: (option ? renderSelectedLeading?.(option) : void 0) ?? "",
                      trailing: /* @__PURE__ */ jsxRuntimeExports.jsx(
                        "button",
                        {
                          type: "button",
                          onClick: handleRemove(value.toString()),
                          className: cn(variants$1(), "size-5"),
                          children: /* @__PURE__ */ jsxRuntimeExports.jsx(X$1, {})
                        }
                      )
                    }
                  );
                })(),
                !value?.length && /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { className: "ml-2 text-muted-foreground", children: placeholder })
              ]
            }
          ),
          helperText && /* @__PURE__ */ jsxRuntimeExports.jsx(HelperText, { error, children: helperText })
        ] })
      ]
    }
  );
}

function AutocompleteFormField({
  control,
  name,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Controller,
    {
      control,
      name,
      render: ({ field, fieldState }) => /* @__PURE__ */ jsxRuntimeExports.jsx(
        Autocomplete,
        {
          "data-cy": field.name,
          ...props,
          id: props.id ?? field.name,
          "aria-invalid": fieldState.invalid,
          name: field.name,
          value: props.mode === "multiple" ? lodashExports.isArray(field.value) ? field.value : [] : field.value,
          disabled: field.disabled,
          onBlur: field.onBlur,
          onValueChange: (value) => {
            field.onChange(value);
            props.onValueChange?.(value);
          },
          error: fieldState.invalid,
          helperText: fieldState.error?.message
        }
      )
    }
  );
}

function DateField({
  value,
  label,
  helperText,
  error,
  onValueChange,
  placeholder,
  className,
  ...props
}) {
  const [open, setOpen] = __mf_38(false);
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(Popover, { open, onOpenChange: setOpen, children: [
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      PopoverTrigger,
      {
        "data-slot": "date-field-trigger",
        className: cn("w-full", className),
        children: /* @__PURE__ */ jsxRuntimeExports.jsx(
          DateFieldTrigger,
          {
            open,
            label,
            helperText,
            placeholder,
            value,
            error
          }
        )
      }
    ),
    /* @__PURE__ */ jsxRuntimeExports.jsx(
      PopoverContent,
      {
        className: "flex flex-col gap-0.5 w-max p-1 items-end",
        align: "start",
        "data-slot": "date-field-content",
        children: /* @__PURE__ */ jsxRuntimeExports.jsx(
          DateFieldContent,
          {
            value,
            onValueChange,
            onOpen: setOpen,
            ...props
          }
        )
      }
    )
  ] });
}
function DateFieldContent({
  value,
  onValueChange,
  onOpen,
  showTimePicker,
  ...props
}) {
  const handleSelection = __mf_24(
    (selected) => {
      onValueChange?.(selected);
      if (!showTimePicker) {
        onOpen(false);
      }
    },
    [onValueChange, showTimePicker, onOpen]
  );
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Calendar,
    {
      ...props,
      mode: "single",
      selected: value,
      onSelect: handleSelection
    }
  );
}
function DateFieldTrigger({
  value,
  label,
  helperText,
  error,
  placeholder,
  className,
  open,
  required,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsxs(
    "div",
    {
      "data-cy": props.name ?? "date-field",
      "data-slot": "date-field",
      className: cn("grid w-full gap-1.5", className),
      children: [
        /* @__PURE__ */ jsxRuntimeExports.jsx(Label, { withAsterisk: required, children: label }),
        /* @__PURE__ */ jsxRuntimeExports.jsxs("div", { className: "flex flex-col gap-0.5", children: [
          /* @__PURE__ */ jsxRuntimeExports.jsxs(
            "div",
            {
              className: cn(
                "flex justify-between gap-2 p-1 px-3 flex-wrap min-h-11 items-center w-full rounded-sm border transition-all has-disabled:pointer-events-none has-disabled:cursor-not-allowed",
                "focus-within:border-primary focus-within:ring-[3px] focus-within:ring-primary/30",
                "hover:border-primary hover:ring-[2px] hover:ring-primary/25",
                "[&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-5 [&_svg]:text-foreground/80",
                "bg-muted/5 has-disabled:bg-transparent",
                "cursor-pointer disabled:cursor-not-allowed",
                {
                  "border-primary ring-[3px] ring-primary/30": open,
                  "bg-primary/10": !!value,
                  "border-error": error
                }
              ),
              children: [
                value ? /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { children: format(value, "P") }) : /* @__PURE__ */ jsxRuntimeExports.jsx(Typography, { "data-placeholder": true, className: "text-foreground/80", children: placeholder }),
                /* @__PURE__ */ jsxRuntimeExports.jsx(Calendar$1, {})
              ]
            }
          ),
          helperText && /* @__PURE__ */ jsxRuntimeExports.jsx(HelperText, { error, children: helperText })
        ] })
      ]
    }
  );
}

function DateFormField({
  control,
  name,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(
    Controller,
    {
      control,
      name,
      render: ({ field, fieldState }) => /* @__PURE__ */ jsxRuntimeExports.jsx(
        DateField,
        {
          "data-cy": field.name,
          ...props,
          id: props.id ?? field.name,
          "aria-invalid": fieldState.invalid,
          value: field.value,
          disabled: field.disabled,
          onDayBlur: field.onBlur,
          onValueChange: field.onChange,
          error: fieldState.invalid,
          helperText: fieldState.error?.message
        }
      )
    }
  );
}

// src/slot.tsx
// @__NO_SIDE_EFFECTS__
function createSlot(ownerName) {
  const SlotClone = /* @__PURE__ */ createSlotClone(ownerName);
  const Slot2 = __mf_16((props, forwardedRef) => {
    const { children, ...slotProps } = props;
    const childrenArray = __mf_1.toArray(children);
    const slottable = childrenArray.find(isSlottable);
    if (slottable) {
      const newElement = slottable.props.children;
      const newChildren = childrenArray.map((child) => {
        if (child === slottable) {
          if (__mf_1.count(newElement) > 1) return __mf_1.only(null);
          return __mf_17(newElement) ? newElement.props.children : null;
        } else {
          return child;
        }
      });
      return /* @__PURE__ */ jsxRuntimeExports.jsx(SlotClone, { ...slotProps, ref: forwardedRef, children: __mf_17(newElement) ? __mf_12(newElement, void 0, newChildren) : null });
    }
    return /* @__PURE__ */ jsxRuntimeExports.jsx(SlotClone, { ...slotProps, ref: forwardedRef, children });
  });
  Slot2.displayName = `${ownerName}.Slot`;
  return Slot2;
}
// @__NO_SIDE_EFFECTS__
function createSlotClone(ownerName) {
  const SlotClone = __mf_16((props, forwardedRef) => {
    const { children, ...slotProps } = props;
    if (__mf_17(children)) {
      const childrenRef = getElementRef(children);
      const props2 = mergeProps(slotProps, children.props);
      if (children.type !== __mf_3) {
        props2.ref = forwardedRef ? composeRefs(forwardedRef, childrenRef) : childrenRef;
      }
      return __mf_12(children, props2);
    }
    return __mf_1.count(children) > 1 ? __mf_1.only(null) : null;
  });
  SlotClone.displayName = `${ownerName}.SlotClone`;
  return SlotClone;
}
var SLOTTABLE_IDENTIFIER = Symbol("radix.slottable");
function isSlottable(child) {
  return __mf_17(child) && typeof child.type === "function" && "__radixId" in child.type && child.type.__radixId === SLOTTABLE_IDENTIFIER;
}
function mergeProps(slotProps, childProps) {
  const overrideProps = { ...childProps };
  for (const propName in childProps) {
    const slotPropValue = slotProps[propName];
    const childPropValue = childProps[propName];
    const isHandler = /^on[A-Z]/.test(propName);
    if (isHandler) {
      if (slotPropValue && childPropValue) {
        overrideProps[propName] = (...args) => {
          const result = childPropValue(...args);
          slotPropValue(...args);
          return result;
        };
      } else if (slotPropValue) {
        overrideProps[propName] = slotPropValue;
      }
    } else if (propName === "style") {
      overrideProps[propName] = { ...slotPropValue, ...childPropValue };
    } else if (propName === "className") {
      overrideProps[propName] = [slotPropValue, childPropValue].filter(Boolean).join(" ");
    }
  }
  return { ...slotProps, ...overrideProps };
}
function getElementRef(element) {
  let getter = Object.getOwnPropertyDescriptor(element.props, "ref")?.get;
  let mayWarn = getter && "isReactWarning" in getter && getter.isReactWarning;
  if (mayWarn) {
    return element.ref;
  }
  getter = Object.getOwnPropertyDescriptor(element, "ref")?.get;
  mayWarn = getter && "isReactWarning" in getter && getter.isReactWarning;
  if (mayWarn) {
    return element.props.ref;
  }
  return element.props.ref || element.ref;
}

// src/primitive.tsx
var NODES = [
  "a",
  "button",
  "div",
  "form",
  "h2",
  "h3",
  "img",
  "input",
  "label",
  "li",
  "nav",
  "ol",
  "p",
  "select",
  "span",
  "svg",
  "ul"
];
var Primitive = NODES.reduce((primitive, node) => {
  const Slot = createSlot(`Primitive.${node}`);
  const Node = __mf_16((props, forwardedRef) => {
    const { asChild, ...primitiveProps } = props;
    const Comp = asChild ? Slot : node;
    if (typeof window !== "undefined") {
      window[Symbol.for("radix-ui")] = true;
    }
    return /* @__PURE__ */ jsxRuntimeExports.jsx(Comp, { ...primitiveProps, ref: forwardedRef });
  });
  Node.displayName = `Primitive.${node}`;
  return { ...primitive, [node]: Node };
}, {});

var POPOVER_NAME = "Popover";
var [createPopoverContext] = createContextScope(POPOVER_NAME, [
  createPopperScope
]);
var usePopperScope = createPopperScope();
var [PopoverProvider, usePopoverContext] = createPopoverContext(POPOVER_NAME);
var Popover$1 = (props) => {
  const {
    __scopePopover,
    children,
    open: openProp,
    defaultOpen,
    onOpenChange,
    modal = false
  } = props;
  const popperScope = usePopperScope(__scopePopover);
  const triggerRef = __mf_37(null);
  const [hasCustomAnchor, setHasCustomAnchor] = __mf_38(false);
  const [open, setOpen] = useControllableState({
    prop: openProp,
    defaultProp: defaultOpen ?? false,
    onChange: onOpenChange,
    caller: POPOVER_NAME
  });
  return /* @__PURE__ */ jsxRuntimeExports.jsx(Root2$1, { ...popperScope, children: /* @__PURE__ */ jsxRuntimeExports.jsx(
    PopoverProvider,
    {
      scope: __scopePopover,
      contentId: useId(),
      triggerRef,
      open,
      onOpenChange: setOpen,
      onOpenToggle: __mf_24(() => setOpen((prevOpen) => !prevOpen), [setOpen]),
      hasCustomAnchor,
      onCustomAnchorAdd: __mf_24(() => setHasCustomAnchor(true), []),
      onCustomAnchorRemove: __mf_24(() => setHasCustomAnchor(false), []),
      modal,
      children
    }
  ) });
};
Popover$1.displayName = POPOVER_NAME;
var ANCHOR_NAME = "PopoverAnchor";
var PopoverAnchor$1 = __mf_16(
  (props, forwardedRef) => {
    const { __scopePopover, ...anchorProps } = props;
    const context = usePopoverContext(ANCHOR_NAME, __scopePopover);
    const popperScope = usePopperScope(__scopePopover);
    const { onCustomAnchorAdd, onCustomAnchorRemove } = context;
    __mf_28(() => {
      onCustomAnchorAdd();
      return () => onCustomAnchorRemove();
    }, [onCustomAnchorAdd, onCustomAnchorRemove]);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(Anchor, { ...popperScope, ...anchorProps, ref: forwardedRef });
  }
);
PopoverAnchor$1.displayName = ANCHOR_NAME;
var TRIGGER_NAME = "PopoverTrigger";
var PopoverTrigger$1 = __mf_16(
  (props, forwardedRef) => {
    const { __scopePopover, ...triggerProps } = props;
    const context = usePopoverContext(TRIGGER_NAME, __scopePopover);
    const popperScope = usePopperScope(__scopePopover);
    const composedTriggerRef = useComposedRefs(forwardedRef, context.triggerRef);
    const trigger = /* @__PURE__ */ jsxRuntimeExports.jsx(
      Primitive.button,
      {
        type: "button",
        "aria-haspopup": "dialog",
        "aria-expanded": context.open,
        "aria-controls": context.contentId,
        "data-state": getState(context.open),
        ...triggerProps,
        ref: composedTriggerRef,
        onClick: composeEventHandlers(props.onClick, context.onOpenToggle)
      }
    );
    return context.hasCustomAnchor ? trigger : /* @__PURE__ */ jsxRuntimeExports.jsx(Anchor, { asChild: true, ...popperScope, children: trigger });
  }
);
PopoverTrigger$1.displayName = TRIGGER_NAME;
var PORTAL_NAME = "PopoverPortal";
var [PortalProvider, usePortalContext] = createPopoverContext(PORTAL_NAME, {
  forceMount: void 0
});
var PopoverPortal = (props) => {
  const { __scopePopover, forceMount, children, container } = props;
  const context = usePopoverContext(PORTAL_NAME, __scopePopover);
  return /* @__PURE__ */ jsxRuntimeExports.jsx(PortalProvider, { scope: __scopePopover, forceMount, children: /* @__PURE__ */ jsxRuntimeExports.jsx(Presence, { present: forceMount || context.open, children: /* @__PURE__ */ jsxRuntimeExports.jsx(Portal$2, { asChild: true, container, children }) }) });
};
PopoverPortal.displayName = PORTAL_NAME;
var CONTENT_NAME = "PopoverContent";
var PopoverContent$1 = __mf_16(
  (props, forwardedRef) => {
    const portalContext = usePortalContext(CONTENT_NAME, props.__scopePopover);
    const { forceMount = portalContext.forceMount, ...contentProps } = props;
    const context = usePopoverContext(CONTENT_NAME, props.__scopePopover);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(Presence, { present: forceMount || context.open, children: context.modal ? /* @__PURE__ */ jsxRuntimeExports.jsx(PopoverContentModal, { ...contentProps, ref: forwardedRef }) : /* @__PURE__ */ jsxRuntimeExports.jsx(PopoverContentNonModal, { ...contentProps, ref: forwardedRef }) });
  }
);
PopoverContent$1.displayName = CONTENT_NAME;
var Slot = createSlot("PopoverContent.RemoveScroll");
var PopoverContentModal = __mf_16(
  (props, forwardedRef) => {
    const context = usePopoverContext(CONTENT_NAME, props.__scopePopover);
    const contentRef = __mf_37(null);
    const composedRefs = useComposedRefs(forwardedRef, contentRef);
    const isRightClickOutsideRef = __mf_37(false);
    __mf_28(() => {
      const content = contentRef.current;
      if (content) return hideOthers(content);
    }, []);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(ReactRemoveScroll, { as: Slot, allowPinchZoom: true, children: /* @__PURE__ */ jsxRuntimeExports.jsx(
      PopoverContentImpl,
      {
        ...props,
        ref: composedRefs,
        trapFocus: context.open,
        disableOutsidePointerEvents: true,
        onCloseAutoFocus: composeEventHandlers(props.onCloseAutoFocus, (event) => {
          event.preventDefault();
          if (!isRightClickOutsideRef.current) context.triggerRef.current?.focus();
        }),
        onPointerDownOutside: composeEventHandlers(
          props.onPointerDownOutside,
          (event) => {
            const originalEvent = event.detail.originalEvent;
            const ctrlLeftClick = originalEvent.button === 0 && originalEvent.ctrlKey === true;
            const isRightClick = originalEvent.button === 2 || ctrlLeftClick;
            isRightClickOutsideRef.current = isRightClick;
          },
          { checkForDefaultPrevented: false }
        ),
        onFocusOutside: composeEventHandlers(
          props.onFocusOutside,
          (event) => event.preventDefault(),
          { checkForDefaultPrevented: false }
        )
      }
    ) });
  }
);
var PopoverContentNonModal = __mf_16(
  (props, forwardedRef) => {
    const context = usePopoverContext(CONTENT_NAME, props.__scopePopover);
    const hasInteractedOutsideRef = __mf_37(false);
    const hasPointerDownOutsideRef = __mf_37(false);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(
      PopoverContentImpl,
      {
        ...props,
        ref: forwardedRef,
        trapFocus: false,
        disableOutsidePointerEvents: false,
        onCloseAutoFocus: (event) => {
          props.onCloseAutoFocus?.(event);
          if (!event.defaultPrevented) {
            if (!hasInteractedOutsideRef.current) context.triggerRef.current?.focus();
            event.preventDefault();
          }
          hasInteractedOutsideRef.current = false;
          hasPointerDownOutsideRef.current = false;
        },
        onInteractOutside: (event) => {
          props.onInteractOutside?.(event);
          if (!event.defaultPrevented) {
            hasInteractedOutsideRef.current = true;
            if (event.detail.originalEvent.type === "pointerdown") {
              hasPointerDownOutsideRef.current = true;
            }
          }
          const target = event.target;
          const targetIsTrigger = context.triggerRef.current?.contains(target);
          if (targetIsTrigger) event.preventDefault();
          if (event.detail.originalEvent.type === "focusin" && hasPointerDownOutsideRef.current) {
            event.preventDefault();
          }
        }
      }
    );
  }
);
var PopoverContentImpl = __mf_16(
  (props, forwardedRef) => {
    const {
      __scopePopover,
      trapFocus,
      onOpenAutoFocus,
      onCloseAutoFocus,
      disableOutsidePointerEvents,
      onEscapeKeyDown,
      onPointerDownOutside,
      onFocusOutside,
      onInteractOutside,
      ...contentProps
    } = props;
    const context = usePopoverContext(CONTENT_NAME, __scopePopover);
    const popperScope = usePopperScope(__scopePopover);
    useFocusGuards();
    return /* @__PURE__ */ jsxRuntimeExports.jsx(
      FocusScope,
      {
        asChild: true,
        loop: true,
        trapped: trapFocus,
        onMountAutoFocus: onOpenAutoFocus,
        onUnmountAutoFocus: onCloseAutoFocus,
        children: /* @__PURE__ */ jsxRuntimeExports.jsx(
          DismissableLayer,
          {
            asChild: true,
            disableOutsidePointerEvents,
            onInteractOutside,
            onEscapeKeyDown,
            onPointerDownOutside,
            onFocusOutside,
            onDismiss: () => context.onOpenChange(false),
            children: /* @__PURE__ */ jsxRuntimeExports.jsx(
              Content$1,
              {
                "data-state": getState(context.open),
                role: "dialog",
                id: context.contentId,
                ...popperScope,
                ...contentProps,
                ref: forwardedRef,
                style: {
                  ...contentProps.style,
                  // re-namespace exposed content custom properties
                  ...{
                    "--radix-popover-content-transform-origin": "var(--radix-popper-transform-origin)",
                    "--radix-popover-content-available-width": "var(--radix-popper-available-width)",
                    "--radix-popover-content-available-height": "var(--radix-popper-available-height)",
                    "--radix-popover-trigger-width": "var(--radix-popper-anchor-width)",
                    "--radix-popover-trigger-height": "var(--radix-popper-anchor-height)"
                  }
                }
              }
            )
          }
        )
      }
    );
  }
);
var CLOSE_NAME = "PopoverClose";
var PopoverClose = __mf_16(
  (props, forwardedRef) => {
    const { __scopePopover, ...closeProps } = props;
    const context = usePopoverContext(CLOSE_NAME, __scopePopover);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(
      Primitive.button,
      {
        type: "button",
        ...closeProps,
        ref: forwardedRef,
        onClick: composeEventHandlers(props.onClick, () => context.onOpenChange(false))
      }
    );
  }
);
PopoverClose.displayName = CLOSE_NAME;
var ARROW_NAME = "PopoverArrow";
var PopoverArrow = __mf_16(
  (props, forwardedRef) => {
    const { __scopePopover, ...arrowProps } = props;
    const popperScope = usePopperScope(__scopePopover);
    return /* @__PURE__ */ jsxRuntimeExports.jsx(Arrow, { ...popperScope, ...arrowProps, ref: forwardedRef });
  }
);
PopoverArrow.displayName = ARROW_NAME;
function getState(open) {
  return open ? "open" : "closed";
}
var Root2 = Popover$1;
var Anchor2 = PopoverAnchor$1;
var Trigger = PopoverTrigger$1;
var Portal = PopoverPortal;
var Content2 = PopoverContent$1;

function Popover({ ...props }) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(Root2, { "data-slot": "popover", ...props });
}
function PopoverTrigger({
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(Trigger, { "data-slot": "popover-trigger", ...props });
}
function PopoverContent({
  className,
  align = "center",
  sideOffset = 4,
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(Portal, { children: /* @__PURE__ */ jsxRuntimeExports.jsx(
    Content2,
    {
      "data-slot": "popover-content",
      align,
      sideOffset,
      className: cn(
        "bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2 z-50 w-72 origin-(--radix-popover-content-transform-origin) rounded-md border p-4 shadow-md outline-hidden",
        className
      ),
      ...props
    }
  ) });
}
function PopoverAnchor({
  ...props
}) {
  return /* @__PURE__ */ jsxRuntimeExports.jsx(Anchor2, { "data-slot": "popover-anchor", ...props });
}

export { AutocompleteFormField as A, Calendar as C, DateFormField as D, Popover as P, CalendarDayButton as a, Chip as b, Command as c, CommandDialog as d, CommandEmpty as e, CommandGroup as f, CommandInput as g, CommandItem as h, CommandList as i, CommandSeparator as j, CommandShortcut as k, PopoverAnchor as l, PopoverContent as m, PopoverTrigger as n, Calendar$1 as o, isValid as p, format as q };
