import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.extend(relativeTime);


const isEmpty = (value) => !value;


export const formatDateTime = (value) =>

  isEmpty(value)
    ? "-"
    : dayjs(value).format("DD MMM YYYY, hh:mm A");


export const formatDate = (value) =>

  isEmpty(value)
    ? "-"
    : dayjs(value).format("DD MMM YYYY");


export const formatRelativeTime = (value) =>

  isEmpty(value)
    ? "-"
    : dayjs(value).fromNow();