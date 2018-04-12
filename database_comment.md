#####问题表question

字段 | 说明
:---:|:---:
_id | 问题id
question_text | 问题文本
question_img | 问题图片
question_subject | 问题科目（1表示科目1,表示科目4）


#####答案表answer

字段 | 说明
:---:|:---:
_id | 答案id
answer_text | 答案文本
is_ok | 是否是正确答案(1表示正确，0表示错误)
question_id | 问题id

#####问题分析表analysis

字段 | 说明
:---:|:---:
_id | 分析id
analysis_text | 分析文本
user_id | 分析人的id
question_id | 问题id

#####问题类型question_type

字段 | 说明
:---:|:---:
_id | 问题类型id
question_type | 类型
question_id | 问题id


#####类型question_type

类型 | 说明
:---:|:---:
1   | 判断题
2   | 文字题
3   | 图片题
4   | 单选题
5   | 多选题
11  | 距离题
12  | 罚款题
13  | 速度题
14  | 标线题
15  | 标志题
16  | 手势题
17  | 信号题
18  | 计分题
19  | 酒驾题
20  | 灯光题
21  | 仪表题
22  | 装置题
23  | 路况题


