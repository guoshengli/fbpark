package com.fbpark.rest.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fbpark.rest.model.User;
import com.google.common.base.Strings;

@Repository("userDao")
@SuppressWarnings("unchecked")
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {
	public UserDaoImpl() {
		super(User.class);
	}

	public User loginUser(String email, String password) {
		String hql = "from User where email = ? and password = ? and status='enabled'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, email);
		query.setString(1, password);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = list.get(0);
		}
		return user;
	}

	public User loginByPhone(String zone, String phone, String password) {
		String hql = "from User where 1=1 and zone = ? and phone=? and password = ? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, zone);
		query.setString(1, phone);
		query.setString(2, password);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public void disableUser(Long key) {
		String hql = "update User set status=? where id=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, "disabled");
		query.setLong(1, key.longValue());
		query.executeUpdate();
	}

	public User getUserByUsernameAndEmail(String username, String email) {
		String hql = "from User where username=? and email=?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, username);
		query.setString(1, email);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public User getUserByZoneAndPhone(String zone, String phone) {
		String hql = "from User where zone=? and phone=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, zone);
		query.setString(1, phone);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public User getUserByIdcard(String idcard) {
		String hql = "from User where identify_card=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, idcard);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public User getUserByUserName(String userName) {
		String hql = "from User where username=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, userName);
		User user = null;
		List<User> list = query.list();
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public List<User> getRandomUser() {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from User ";
		Random r = new Random();
		Query query = session.createQuery(hql);
		int size = query.list().size();
		query.setMaxResults(10);
		query.setFirstResult(r.nextInt(Math.abs(size - 10)) + 1);
		List<User> userList = query.list();
		return userList;
	}

	public List<User> getUsersByStoryIdAndNull(Long storyId, int count) {
		String hql = "select u from User u,Likes l where l.userId=u.id and l.storyId=? order by l.createTime desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setLong(0, storyId.longValue());
		query.setMaxResults(count);
		List<User> list = query.list();
		return list;
	}

	public List<User> getUsersByStoryIdAndUserId(Long storyId, Long likesId, String createTime, int count,
			int identify) {
		String hql = "";
		List<User> userList = new ArrayList<User>();

		Session session = getSessionFactory().getCurrentSession();
		if (identify == 1) {
			hql = "select u from User u,Likes l where l.userId=u.id and l.storyId=? and l.createTime >= ? and l.id !=? order by l.createTime";
			Query query = session.createQuery(hql);
			query.setLong(0, storyId.longValue());
			query.setString(1, createTime);
			query.setLong(2, likesId.longValue());
			query.setMaxResults(count);
			userList = query.list();
			Collections.reverse(userList);
		} else if (identify == 2) {
			hql = "select u from User u,Likes l where l.userId=u.id and l.storyId=? and l.createTime <= ? and l.id !=? order by l.createTime desc";
			Query query = session.createQuery(hql);
			query.setLong(0, storyId.longValue());
			query.setString(1, createTime);
			query.setLong(2, likesId.longValue());
			query.setMaxResults(count);
			userList = query.list();
		}

		return userList;
	}

	public List<User> getRepostUsersByStoryId(Long storyId, int count) {
		String hql = "select u from User u,Republish r where r.userId=u.id and r.storyId=? order by r.createTime desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setLong(0, storyId.longValue());
		query.setMaxResults(count);
		List<User> list = query.list();
		return list;
	}

	public List<User> getRepostUsersByStoryIdAndRepost(Long storyId, Long repostId, String createTime, int count,
			int identify) {
		String hql = "";
		List<User> userList = new ArrayList<User>();

		Session session = getSessionFactory().getCurrentSession();
		if (identify == 1) {
			hql = "select u from User u,Republish r where r.userId=u.id and r.storyId=? and l.createTime >= ? and r.id != ? order by r.createTime";
			Query query = session.createQuery(hql);
			query.setLong(0, storyId.longValue());
			query.setString(1, createTime);
			query.setLong(2, repostId.longValue());
			query.setMaxResults(count);
			userList = query.list();
			Collections.reverse(userList);
		} else if (identify == 2) {
			hql = "select u from User u,Republish r where r.userId=u.id and r.storyId=? and l.createTime <= ? and r.id != ? order by r.createTime desc";
			Query query = session.createQuery(hql);
			query.setLong(0, storyId.longValue());
			query.setString(1, createTime);
			query.setLong(2, repostId.longValue());
			query.setMaxResults(count);
			userList = query.list();
		}

		return userList;
	}

	public void updateUserByUserType(Long userId, String user_type) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "update User set user_type=? where id=?";
		Query query = session.createQuery(hql);
		query.setString(0, user_type).setLong(1, userId.longValue());
		query.executeUpdate();
	}

	public List<User> getUserByName(String username) {
		String hql = "from User where username like :username and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("username", "%" + username + "%");
		List<User> list = query.list();
		return list;
	}

	public User getUserByPhoneAndZone(String zone, String phone) {
		String hql = "from User where zone = ? and phone = ? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString(0, zone);
		query.setString(1, phone);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	public List<User> getUserByUserType() {
		String hql = "from User where 1=1 and user_type='super_admin' or user_type='admin' and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		List<User> list = query.list();
		return list;
	}

	public List<User> getUserByUserType(String user_type) {
		String hql = "from User where 1=1 and level=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0, user_type);
		List<User> list = query.list();
		return list;
	}

	public List<User> getUserByPhoneOrIdcard(String phone, String idcard) {
		String hql = "from User where 1=1 and (phone=? or identity_card=?) and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0, phone).setString(1, idcard);
		List<User> list = query.list();
		return list;
	}

	public List<User> getUserRandom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUserList(int count, int page, String phone, String username, String idcard, String club_name,
			String start_time, String end_time, String birthday_order, String time_order) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		int start = 0;
		if (page == 0) {
			start = page * count;
		} else {
			start = (page - 1) * count;
		}
		if (!Strings.isNullOrEmpty(phone)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable'  and phone like :phone " + "order by id desc";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable'  and phone like :phone "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1  and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and phone like :phone "
						+ " and club_name like :club_name" + "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and phone like :phone " + "  order by birthday "
						+ birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and phone like :phone "
						+ " and club_name like :club_name" + "  order by create_time " + time_order;
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}

		} else if (!Strings.isNullOrEmpty(username)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username " + "order by id desc";
				query = session.createQuery(hql).setString("username", "%" + username + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name" + "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username " + "  order by birthday "
						+ birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name" + "  order by create_time " + time_order;
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}

		} else if (!Strings.isNullOrEmpty(idcard)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ "order by id desc";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name" + "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name" + "  order by create_time " + time_order;
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}

			hql = "from User where 1=1 and identity_card like :identity_card and status='enable' order by id desc";
			query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setFirstResult(start)
					.setMaxResults(count);
		} else {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + "order by id desc";
				query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and create_time between :start_time and :end_time"
						+ "  order by id desc ";
				query = session.createQuery(hql).setString("start_time", start_time).setString("end_time", end_time)
						.setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("start_time", start_time).setString("end_time", end_time)
						.setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("start_time", start_time).setString("end_time", end_time)
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and club_name like :club_name"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' " + " and club_name like :club_name"
						+ "  order by create_time " + time_order;
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%").setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and status='enable' "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			}

		}

		List<User> list = query.list();
		return list;
	}

	@Override
	public List<User> getUserListByUsername(int count, int page, String username) {

		String hql = "from User where 1=1 and status='enable' and username like '%?%' order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start = (page - 1) * count + 1;
		Query query = session.createQuery(hql).setString(0, username).setFirstResult(start).setMaxResults(count);
		List<User> list = query.list();
		return list;
	}

	@Override
	public List<User> getUserListByPhone(int count, int page, String phone) {
		String hql = "from User where 1=1 and status='enable' and phone=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start = (page - 1) * count + 1;
		Query query = session.createQuery(hql).setString(0, phone).setFirstResult(start).setMaxResults(count);
		List<User> list = query.list();
		return list;
	}

	@Override
	public int getUserCount(String phone, String username, String idcard, String club_name, String start_time,
			String end_time) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		if (!Strings.isNullOrEmpty(phone)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and phone like :phone";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and club_name like :club_name and phone like :phone";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%").setString("phone",
						"%" + phone + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and phone like :phone and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and club_name like :club_name and phone like :phone and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("phone", "%" + phone + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}

		} else if (!Strings.isNullOrEmpty(username)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and username like :username";
				query = session.createQuery(hql).setString("username", "%" + username + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and username like :username and club_name like :club_name ";
				query = session.createQuery(hql).setString("username", "%" + username + "%").setString("club_name",
						"%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and username like :username and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and username like :username and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}

		} else if (!Strings.isNullOrEmpty(idcard)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and identity_card like :identity_card";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and identity_card like :identity_card and club_name like :club_name ";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setString("club_name",
						"%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and identity_card like :identity_card and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and identity_card like :identity_card and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}
		} else {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable'";
				query = session.createQuery(hql);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and club_name like :club_name ";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setString("club_name",
						"%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			}

		}
		List list = query.list();
		int count = 0;
		if (list != null && list.size() > 0) {
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	}

	@Override
	public int getUserCountByUsername(String username) {
		String hql = "from User where 1=1 and status='enable' and username like :username";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString("username", "%" + username + "%");
		List<User> list = query.list();
		return list.size();
	}

	@Override
	public int getUserCountByPhone(String phone) {
		String hql = "from User where 1=1 and status='enable' and phone=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0, phone);
		List<User> list = query.list();
		return list.size();
	}

	@Override
	public int getUserCountByLevel(String level, String phone, String username, String idcard, String club_name,
			String start_time, String end_time) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		if (!Strings.isNullOrEmpty(phone)) {
			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and phone like :phone";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and club_name like :club_name and phone like :phone";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("phone", "%" + phone + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and phone like :phone and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and club_name like :club_name and phone like :phone and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("phone", "%" + phone + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}

		} else if (!Strings.isNullOrEmpty(username)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and username like :username";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and username like :username and club_name like :club_name ";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and username like :username and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and username like :username and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}

		} else if (!Strings.isNullOrEmpty(idcard)) {
			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and identity_card like :identity_card";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%");
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and identity_card like :identity_card and club_name like :club_name ";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and identity_card like :identity_card and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and identity_card like :identity_card and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time);
			}

		} else {
			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=?";
				query = session.createQuery(hql).setString(0, level);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and club_name like :club_name ";
				query = session.createQuery(hql).setString("identity_card", "%" + idcard + "%").setString("club_name",
						"%" + club_name + "%");
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("start_time", start_time)
						.setString("end_time", end_time);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time)) {
				hql = "select count(*) from User where 1=1 and status='enable' and level=? and club_name like :club_name and create_time between :start_time and :end_time";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time);
			}

		}

		List list = query.list();
		int count = 0;
		if (list != null && list.size() > 0) {
			count = Integer.parseInt(list.get(0).toString());
		}
		return count;
	}

	@Override
	public List<User> getUserListByLevel(int count, int page, String level, String phone, String username,
			String idcard, String club_name, String start_time, String end_time, String birthday_order,
			String time_order) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "";
		Query query = null;
		int start = 0;
		if (page == 0) {
			start = page * count;
		} else {
			start = (page - 1) * count;
		}
		if (!Strings.isNullOrEmpty(phone)) {
			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone " + "order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone " + " and club_name like :club_name"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone " + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone " + " and club_name like :club_name"
						+ "  order by create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and phone like :phone "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("phone", "%" + phone + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}

		} else if (!Strings.isNullOrEmpty(username)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username " + "order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username " + " and club_name like :club_name"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username " + "  order by birthday "
						+ birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username " + " and club_name like :club_name"
						+ "  order by create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and username like :username "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("username", "%" + username + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}

		} else if (!Strings.isNullOrEmpty(idcard)) {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card " + "order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by id desc ";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and create_time between :start_time and :end_time" + "  order by birthday " + birthday_order
						+ ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name" + "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card " + "  order by birthday "
						+ birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name" + "  order by create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? and identity_card like :identity_card "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("identity_card", "%" + idcard + "%")
						.setString("club_name", "%" + club_name + "%").setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			}
		} else {

			if (Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time) && Strings.isNullOrEmpty(end_time)
					&& Strings.isNullOrEmpty(birthday_order) && Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? " + "order by id desc";
				query = session.createQuery(hql).setString(0, level).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?" + " and club_name like :club_name order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?"
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?" + " and create_time between :start_time and :end_time"
						+ "  order by id desc ";
				query = session.createQuery(hql).setString(0, level).setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? " + " and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?" + " and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("start_time", start_time)
						.setString("end_time", end_time).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? " + " and club_name like :club_name" + "  order by birthday "
						+ birthday_order;
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && !Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? " + "  order by birthday " + birthday_order + ",create_time "
						+ time_order;
				query = session.createQuery(hql).setString(0, level).setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?"
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by birthday " + birthday_order + ",create_time " + time_order;
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && Strings.isNullOrEmpty(start_time)
					&& Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& !Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=?" + " and club_name like :club_name" + "  order by create_time "
						+ time_order;
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setFirstResult(start).setMaxResults(count);
			} else if (!Strings.isNullOrEmpty(club_name) && !Strings.isNullOrEmpty(start_time)
					&& !Strings.isNullOrEmpty(end_time) && Strings.isNullOrEmpty(birthday_order)
					&& Strings.isNullOrEmpty(time_order)) {
				hql = "from User where 1=1 and level=? "
						+ " and club_name like :club_name and create_time between :start_time and :end_time"
						+ "  order by id desc";
				query = session.createQuery(hql).setString(0, level).setString("club_name", "%" + club_name + "%")
						.setString("start_time", start_time).setString("end_time", end_time).setFirstResult(start)
						.setMaxResults(count);
			}
		}

		List<User> list = query.list();
		return list;
	}

	@Override
	public int getUserCountByIdcard(String idcard) {
		String hql = "from User where 1=1 and status='enable' and identity_card=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0, idcard);
		List<User> list = query.list();
		return list.size();
	}

	@Override
	public List<User> getUserListByIdcard(int count, int page, String idcard) {
		String hql = "from User where 1=1 and status='enable' and identity_card=? order by id desc";
		Session session = getSessionFactory().getCurrentSession();
		int start = (page - 1) * count;
		Query query = session.createQuery(hql).setString(0, idcard).setFirstResult(start).setMaxResults(count);
		List<User> list = query.list();
		return list;
	}

	@Override
	public List<User> getUserListByLevel(String level, String start_time, String end_time) {
		String hql = "";
		Session session = getSessionFactory().getCurrentSession();
		Query query = null;
		if (!Strings.isNullOrEmpty(level) && !Strings.isNullOrEmpty(start_time) && !Strings.isNullOrEmpty(end_time)) {
			hql = "from User where 1=1 and status='enable' and level=? and create_time between :start_time and :end_time order by id desc";
			query = session.createQuery(hql).setString(0, level).setString("start_time", start_time)
					.setString("end_time", end_time);
		} else if (Strings.isNullOrEmpty(level) && !Strings.isNullOrEmpty(start_time)
				&& !Strings.isNullOrEmpty(end_time)) {
			hql = "from User where 1=1 and status='enable' and create_time between :start_time and :end_time order by id desc";
			query = session.createQuery(hql).setString("start_time", start_time).setString("end_time", end_time);
		} else if (Strings.isNullOrEmpty(level) && Strings.isNullOrEmpty(start_time)
				&& Strings.isNullOrEmpty(end_time)) {
			hql = "from User where 1=1 and status='enable' order by id desc";
			query = session.createQuery(hql);
		} else if (!Strings.isNullOrEmpty(level) && Strings.isNullOrEmpty(start_time)
				&& Strings.isNullOrEmpty(end_time)) {
			hql = "from User where 1=1 and status='enable' and level=? order by id desc";
			query = session.createQuery(hql).setString(0, level);
		}

		List<User> list = query.list();
		return list;
	}

	@Override
	public User getUserListByIdcardAndPwd(String idcard, String password) {
		String hql = "from User where 1=1 and identity_card=? and password=? and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);

		query.setString(0, idcard);
		query.setString(1, password);
		List<User> list = query.list();
		User user = null;
		if ((list != null) && (list.size() > 0)) {
			user = (User) list.get(0);
		}
		return user;
	}

	@Override
	public User getFirstUser() {
		String hql = "from User where 1=1 and status='enable'";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setMaxResults(1);
		List<User> uList = query.list();
		User user = null;
		if(uList != null && uList.size() > 0){
			user = uList.get(0);
		}
		return user;
	}

	@Override
	public List<User> getUserList(String create_time) {
		String hql = "from User where 1=1 and status='enable' and create_time <= ?";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql).setString(0, create_time);
		List<User> uList = query.list();
		return uList;
	}

}
