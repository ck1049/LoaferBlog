export enum MessageType {
  TEXT = 1,
  EMOJI = 2,
  IMAGE = 3,
  VIDEO = 4,
  FILE = 5
}

export const messageTypeMap = {
  [MessageType.TEXT]: '文本消息',
  [MessageType.EMOJI]: '表情消息',
  [MessageType.IMAGE]: '图片消息',
  [MessageType.VIDEO]: '视频消息',
  [MessageType.FILE]: '文件消息'
};