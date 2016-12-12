package com.naruto.reserveclassroom.bean;
/*
 * Created by NARUTO on 2016/11/14.
 */

import java.util.List;

public class ClassroomInfoBean {

	/**
	 * @type : com.avos.avoscloud.AVObject objectId : 5821a115a0bb9f0058c6cb67
	 *       updatedAt : 2016-11-14T08:33:51.685Z createdAt :
	 *       2016-11-08T09:55:33.686Z className : Classroom serverData :
	 *       {"@type":"java.util.HashMap","borrowDetail":{"@type":"java.util.HashMap","details":[{"@type":"java.util.HashMap","activityDetail":"xxxx1活动","borrower":"某某1#13555555555","currentState":"1","endT":"2016-11-15
	 *       21:00:00","manager":"某某2#13777777777","multiMedia":"1","startT":"2016-11-15
	 *       18:00:00"},{"@type":"java.util.HashMap","activityDetail":"xxxx2活动","borrower":"某某3#13777777777","currentState":"2","endT":"2016-11-16
	 *       21:00:00","manager":"某某4#13888888888","multiMedia":"1","startT":"2016-11-16
	 *       18:00:00"}]},"people":65,"title":"A1-N106-1480262399"}
	 */

	private String objectId;
	private String updatedAt;
	private String createdAt;
	private String className;
	/**
	 * @type : java.util.HashMap borrowDetail :
	 *       {"@type":"java.util.HashMap","details":[{"@type":"java.util.HashMap","activityDetail":"xxxx1活动","borrower":"某某1#13555555555","currentState":"1","endT":"2016-11-15
	 *       21:00:00","manager":"某某2#13777777777","multiMedia":"1","startT":"2016-11-15
	 *       18:00:00"},{"@type":"java.util.HashMap","activityDetail":"xxxx2活动","borrower":"某某3#13777777777","currentState":"2","endT":"2016-11-16
	 *       21:00:00","manager":"某某4#13888888888","multiMedia":"1","startT":"2016-11-16
	 *       18:00:00"}]} people : 65 stopTime : new Date(1477961596000) title :
	 *       A1-N106-1480262399
	 */

	private ServerDataBean serverData;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ServerDataBean getServerData() {
		return serverData;
	}

	public void setServerData(ServerDataBean serverData) {
		this.serverData = serverData;
	}

	public static class ServerDataBean {
		/**
		 * @type : java.util.HashMap details :
		 *       [{"@type":"java.util.HashMap","activityDetail":"xxxx1活动","borrower":"某某1#13555555555","currentState":"1","endT":"2016-11-15
		 *       21:00:00","manager":"某某2#13777777777","multiMedia":"1","startT":"2016-11-15
		 *       18:00:00"},{"@type":"java.util.HashMap","activityDetail":"xxxx2活动","borrower":"某某3#13777777777","currentState":"2","endT":"2016-11-16
		 *       21:00:00","manager":"某某4#13888888888","multiMedia":"1","startT":"2016-11-16
		 *       18:00:00"}]
		 */

		private BorrowDetailBean borrowDetail;
		private int people;
		private String stopTime;
		private String title;

		public BorrowDetailBean getBorrowDetail() {
			return borrowDetail;
		}

		public void setBorrowDetail(BorrowDetailBean borrowDetail) {
			this.borrowDetail = borrowDetail;
		}

		public int getPeople() {
			return people;
		}

		public void setPeople(int people) {
			this.people = people;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public static class BorrowDetailBean {
			/**
			 * @type : java.util.HashMap activityDetail : xxxx1活动 borrower :
			 *       某某1#13555555555 currentState : 1 endT : 2016-11-15 21:00:00
			 *       manager : 某某2#13777777777 multiMedia : 1 startT :
			 *       2016-11-15 18:00:00
			 */

			private List<DetailsBean> details;

			public List<DetailsBean> getDetails() {
				return details;
			}

			public void setDetails(List<DetailsBean> details) {
				this.details = details;
			}

			public static class DetailsBean {
				private String activityDetail;
				private String borrower;
				private int currentState;
				private String endT;
				private String manager;
				private int multiMedia;
				private String startT;

				public String getActivityDetail() {
					return activityDetail;
				}

				public void setActivityDetail(String activityDetail) {
					this.activityDetail = activityDetail;
				}

				public String getBorrower() {
					return borrower;
				}

				public void setBorrower(String borrower) {
					this.borrower = borrower;
				}

				public int getCurrentState() {
					return currentState;
				}

				public void setCurrentState(int currentState) {
					this.currentState = currentState;
				}

				public String getEndT() {
					return endT;
				}

				public void setEndT(String endT) {
					this.endT = endT;
				}

				public String getManager() {
					return manager;
				}

				public void setManager(String manager) {
					this.manager = manager;
				}

				public int getMultiMedia() {
					return multiMedia;
				}

				public void setMultiMedia(int multiMedia) {
					this.multiMedia = multiMedia;
				}

				public String getStartT() {
					return startT;
				}

				public void setStartT(String startT) {
					this.startT = startT;
				}
			}
		}
	}
}
