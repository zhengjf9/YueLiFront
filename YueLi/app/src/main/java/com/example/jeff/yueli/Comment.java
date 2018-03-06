package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by xumuxin on 2018/3/5.
 */

public class Comment {
    private List<Comment.review> data;
    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public void setdata(List<Comment.review> commentinfo) {
        this.data = commentinfo;
    }

    public String getmsg() {
        return msg;
    }

    public List<Comment.review> getreviews() {
        return data;
    }

    public static class review {
        public static class reply {
            private int user_id;
            private String nickname;
            private String content;

            public int getuserid() {
                return user_id;
            }
            public String getnickname() {
                return nickname;
            }
            public String getcontent() {
                return content;
            }
            public void setuserid(int id) {
                user_id = id;
            }
            public void setnickname(String name) {
                nickname = name;
            }
            public void setcontent(String t) {
                content = t;
            }
        }
        private int comment_id;
        private int user_id;
        private String nickname;
        private String content;
        private reply reply_to;
        private String time;

        public int getcommentid() {
            return comment_id;
        }

        public int getuserid() {
            return user_id;
        }

        public String getnickname() {
            return nickname;
        }

        public String getcontent() {
            return content;
        }

        public reply getreply() {
            return reply_to;
        }

        public String gettime() {
            return time;
        }

        public void setcommentid(int id) {
            comment_id = id;
        }

        public void setuserid(int id) {
            user_id = id;
        }


        public void setnickname(String name) {
            nickname = name;
        }

        public void setcontent(String t) {
            content = t;
        }

        public void setreply(reply r) {
            reply_to = r;
        }

        public void settimey(String t) {
            time = t;
        }

    }
}
