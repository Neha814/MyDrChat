package com.macrew.functions;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.macrew.enitites.enitity;

import android.text.Html;
import android.util.Log;

public class Functions {

	JSONParser json = new JSONParser();

	// public static String uri = "http://dev.macrew.net/my_dr_app/api/";
	public static String uri = "http://mydrchat.com/api/";

	public static ArrayList<HashMap<String, String>> group_members;

	/**
	 * 
	 * @param localArrayList
	 * @return user_id
	 * 
	 *         web service at the time of regidtration. when user enters his
	 *         phone number
	 */

	public HashMap<String, String> user_login(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "user_login.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				JSONObject localJSONObject2 = localJSONObject
						.getJSONObject("data");
				localHashMap.put("user_id",
						localJSONObject2.getString("user_id"));
				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * web service when user enters registrstion details.
	 */
	public HashMap<String, String> user_reg_detail(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "user_login.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * 
	 * @param localArrayList
	 * @return
	 * 
	 *         web service for code verification
	 */

	public HashMap<String, String> verification(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "user_login.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * web service for user login
	 */
	public HashMap<String, String> login(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "user_login.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				JSONObject localJSONObject2 = localJSONObject
						.getJSONObject("data");

				if (localJSONObject2.getString("mode").equals("User")) {
					localHashMap.put("user_id",
							localJSONObject2.getString("user_id"));
				} else if (localJSONObject2.getString("mode").equals("Doctor")) {
					localHashMap.put("user_id",
							localJSONObject2.getString("doctor_id"));
				}
				localHashMap.put("mode", localJSONObject2.getString("mode"));
				return localHashMap;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * web service to get profile data
	 */

	public HashMap<String, String> profile(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json
							.makeHttpRequest(this.uri
									+ "get_profile_detail.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				JSONObject localJSONObject2 = localJSONObject
						.getJSONObject("data");
				localHashMap.put("name", localJSONObject2.getString("name"));
				localHashMap.put("mobile_number",
						localJSONObject2.getString("mobile_number"));
				localHashMap.put("address",
						localJSONObject2.getString("address"));
				localHashMap.put("landline_number",
						localJSONObject2.getString("landline_number"));
				localHashMap
						.put("status", localJSONObject2.getString("status"));
				localHashMap.put("user_type",
						localJSONObject2.getString("user_type"));
				localHashMap.put("profile_image",
						localJSONObject2.getString("profile_image"));
				localHashMap.put("email", localJSONObject2.getString("email"));

				return localHashMap;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * web service to get doctor list
	 */

	public ArrayList doctor_list(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "doctor_plan_price.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("specialty", c.getString("specialty"));
					localHashMap2.put("profile_image",
							c.getString("profile_image"));
					localHashMap2.put("mobile_number",
							c.getString("mobile_number"));
					localHashMap2.put("doctor_id", c.getString("doctor_id"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}

	/**
	 * web service to get doctor message plan
	 */

	public ArrayList doctor_msg_plan(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "doctor_plan_price.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("plan_id", c.getString("plan_id"));
					localHashMap2.put("price", c.getString("price"));
					localHashMap2.put("number_of_message",
							c.getString("number_of_message"));
					localHashMap2.put("doctor_id", c.getString("doctor_id"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}

	/**
	 * payment
	 */

	public HashMap<String, String> payment(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "doctor_plan_price.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * invite users
	 */

	public HashMap<String, String> invite_users(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "invite_users.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	/**
	 * to get user contacts
	 */

	public ArrayList doctor_data(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "doctor_data.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("user_id", c.getString("user_id"));
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number",
							c.getString("mobile_number"));
					localHashMap2.put("address", c.getString("address"));
					localHashMap2.put("landline_number",
							c.getString("landline_number"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2.put("user_type", c.getString("user_type"));
					localHashMap2.put("profile_image",
							c.getString("profile_image"));
					localHashMap2.put("email", c.getString("email"));
					localHashMap2.put("specialty", c.getString("specialty"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}

	/**
	 * chat list
	 */

	public ArrayList chat_log(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "chat_log.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("msg_id", c.getString("msg_id"));
					localHashMap2.put("sender_id", c.getString("sender_id"));
					localHashMap2
							.put("reciever_id", c.getString("reciever_id"));
					localHashMap2.put("message_text",
							c.getString("message_text"));
					localHashMap2.put("attachment_id",
							c.getString("attachment_id"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2
							.put("read_status", c.getString("read_status"));
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("attachment_type",
							c.getString("attachment_type"));
					localHashMap2.put("attachment", c.getString("attachment"));
					localHashMap2.put("profile_image",
							c.getString("profile_image"));
					localHashMap2.put("user_id", c.getString("user_id"));
					localHashMap2.put("user_app_status",
							c.getString("user_app_status"));
					localHashMap2.put("specialty", c.getString("specialty"));
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number",
							c.getString("mobile_number"));
					localHashMap2.put("unread_msgs",
							c.getString("unread_msgs"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}

	/**
	 * web service to get doctor profile data
	 */

	public HashMap<String, String> profile_doctor(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json
							.makeHttpRequest(this.uri
									+ "get_profile_detail.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				JSONObject localJSONObject2 = localJSONObject
						.getJSONObject("data");
				localHashMap.put("name", localJSONObject2.getString("name"));
				localHashMap.put("mobile_number",
						localJSONObject2.getString("mobile_number"));
				localHashMap.put("address",
						localJSONObject2.getString("address"));
				localHashMap.put("landline_number",
						localJSONObject2.getString("landline_number"));
				localHashMap
						.put("status", localJSONObject2.getString("status"));
				localHashMap.put("user_type",
						localJSONObject2.getString("user_type"));
				localHashMap.put("profile_image",
						localJSONObject2.getString("profile_image"));
				localHashMap.put("email", localJSONObject2.getString("email"));
				localHashMap.put("speciality",
						localJSONObject2.getString("specialty"));

				return localHashMap;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}

	public ArrayList group(ArrayList localArrayList) {
		group_members = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "view_group.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("group_id", c.getString("group_id"));
					localHashMap2.put("group_name", c.getString("group_name"));
					localHashMap2.put("admin_id", c.getString("admin_id"));
					localHashMap2.put("group_icon", c.getString("group_icon"));
					localHashMap2.put("message_text",
							c.getString("message_text"));
					localHashMap2
							.put("read_status", c.getString("read_status"));
					localHashMap2.put("attachment_type",
							c.getString("attachment_type"));
				
					localHashMap2.put("time", c.getString("time"));
					localHashMap2
							.put("unread_msgs", c.getString("unread_msgs"));

					locallist.add(localHashMap2);

					JSONArray group_member = c.getJSONArray("group_members");

					for (int j = 0; j < group_member.length(); j++) {
						HashMap localHashMap3 = new HashMap();
						JSONObject d = group_member.getJSONObject(j);

						localHashMap3.put("member_id_" + i,
								d.getString("member_id"));
						localHashMap3.put("members_image_" + i,
								d.getString("members_image"));
						localHashMap3.put("members_" + i,
								d.getString("members"));

						group_members.add(localHashMap3);
					}

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}

	/**
	 * web service to update groiup name
	 * 
	 */
	public HashMap<String, String> update_group_name(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "update_group.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				
				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	
	/**
	 * web service to delete group member
	 * 
	 */
	public HashMap<String, String> delete_group_member(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "update_group.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");
				
				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	
	/**
	 * web service to get participants list
	 */
	

	public ArrayList get_participants_list(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "group_members.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {

				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("user_id", c.getString("user_id"));
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number",c.getString("mobile_number"));
					localHashMap2.put("address",c.getString("address"));
					localHashMap2.put("landline_number", c.getString("landline_number"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2.put("user_type", c.getString("user_type"));
					localHashMap2.put("profile_image", c.getString("profile_image"));
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("email", c.getString("email"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}
	

	/**
	 * 
	 * @param localArrayList
	 * @return
	 * 
	 *         web service for forget pasword
	 */

	public HashMap<String, String> forget_password(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "forgot_paswrd.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	
	/**
	 * 
	 * @param localArrayList
	 * @return
	 * 
	 *         web service for add_participants
	 */

	public HashMap<String, String> add_participants(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "group_members.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	/**
	 * 
	 * @param localArrayList
	 * @return
	 * 
	 *         web service for exit group
	 */

	public HashMap<String, String> exit_group(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "exit_group.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	
	/**
	 * receive current chat
	 */
	
	public ArrayList receivecurrentchat(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "receivecurrentchat.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				HashMap localHashMap2 = new HashMap();
				localHashMap2.put("error", localJSONObject.getString("error"));
				locallist.add(localHashMap2);
				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("msg_id", c.getString("msg_id"));
					localHashMap2.put("sender_id", c.getString("sender_id"));
					localHashMap2.put("reciever_id",c.getString("reciever_id"));
					localHashMap2.put("message_text",c.getString("message_text"));
					localHashMap2.put("attachment_id", c.getString("attachment_id"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("attachment_type", c.getString("attachment_type"));
					localHashMap2.put("attachment", c.getString("attachment"));
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number", c.getString("mobile_number"));
					localHashMap2.put("current_status", c.getString("current_status"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}
	
	/**
	 * load archieve messages
	 */
	
	public ArrayList load_archieve_message(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "load_archieve_message.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				HashMap localHashMap2 = new HashMap();
				localHashMap2.put("error", localJSONObject.getString("error"));
				locallist.add(localHashMap2);
				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("msg_id", c.getString("msg_id"));
					localHashMap2.put("sender_id", c.getString("sender_id"));
					localHashMap2.put("reciever_id",c.getString("reciever_id"));
					localHashMap2.put("message_text",c.getString("message_text"));
					localHashMap2.put("attachment_id", c.getString("attachment_id"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("attachment_type", c.getString("attachment_type"));
					localHashMap2.put("attachment", c.getString("attachment"));
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number", c.getString("mobile_number"));
					localHashMap2.put("current_status", c.getString("current_status"));
					try{
					localHashMap2.put("messages_left", c.getString("messages_left"));
					}
					catch(Exception ae){
						Log.e("msg left excptn==",""+ae);
					}
					localHashMap2.put("page", c.getString("page"));
					localHashMap2.put("lastpage", c.getString("lastpage"));

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}
	
	
	/**
	 * load archieve messages
	 */
	
	public ArrayList load_archieve_group_message(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "load_archieve_group_message.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				HashMap localHashMap2 = new HashMap();
				localHashMap2.put("error", localJSONObject.getString("error"));
				locallist.add(localHashMap2);
				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("group_message_id", c.getString("group_message_id"));
					localHashMap2.put("member_id", c.getString("member_id"));
					localHashMap2.put("group_id",c.getString("group_id"));
					localHashMap2.put("message_text",c.getString("message_text"));
					localHashMap2.put("attachment_id", c.getString("attachment_id"));
	
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("attachment_type", c.getString("attachment_type"));
					try{
					localHashMap2.put("attachment", c.getString("attachment"));
					}
					catch(Exception e){
						Log.e("attachment exceptn==",""+e);
					}
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number", c.getString("mobile_number"));
					localHashMap2.put("current_status", c.getString("current_status"));
					localHashMap2.put("sender_id", c.getString("sender_id"));
					localHashMap2.put("group_admin_id", c.getString("group_admin_id"));
					localHashMap2.put("page", c.getString("page"));
					localHashMap2.put("lastpage", c.getString("lastpage"));
					
					try {
					
					JSONArray d = c.getJSONArray("msg_arr");
					
					localHashMap2.put("messages_left", d.getJSONObject(0).get("messages_left"));
					localHashMap2.put("doctor_id", d.getJSONObject(0).get("doctor_id"));
					}
					
					catch(Exception ae){
						Log.e("exception==",""+ae);
					}

					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}
	
	/**
	 * web service for sending reg_id (GCM)
	 */
	public HashMap<String, String> send_registrationId(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "updateDeviceId.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
	/**
	 * receive current chat
	 */
	
	public ArrayList receivegroupchat(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		try {

			JSONObject localJSONObject = new JSONObject(Html
					.fromHtml(
							this.json.makeHttpRequest(this.uri
									+ "receivegroupchat.php?", "POST",
									localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				HashMap localHashMap2 = new HashMap();
				localHashMap2.put("error", localJSONObject.getString("error"));
				locallist.add(localHashMap2);
				return locallist;
			} else if (localJSONObject.getBoolean("status") == true) {

				JSONArray data = localJSONObject.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					HashMap localHashMap2 = new HashMap();
					JSONObject c = data.getJSONObject(i);

					localHashMap2.put("group_message_id", c.getString("group_message_id"));
				
					localHashMap2.put("group_id",c.getString("group_id"));
					localHashMap2.put("message_text",c.getString("message_text"));
					localHashMap2.put("attachment_id", c.getString("attachment_id"));
	
					localHashMap2.put("created", c.getString("created"));
					localHashMap2.put("attachment_type", c.getString("attachment_type"));
					try{
					localHashMap2.put("attachment", c.getString("attachment"));
					}
					catch(Exception e){
						Log.e("attachment exceptn==",""+e);
					}
					localHashMap2.put("name", c.getString("name"));
					localHashMap2.put("mobile_number", c.getString("mobile_number"));
					localHashMap2.put("status", c.getString("status"));
					localHashMap2.put("sender_id", c.getString("sender_id"));
					localHashMap2.put("current_status", c.getString("current_status"));
					
				
					locallist.add(localHashMap2);

				}

				return locallist;

			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);

			return locallist;

		}

		return locallist;

	}
	
	/**
	 * reset badge
	 */
	
	public HashMap<String, String> resetbadge(ArrayList localArrayList) {
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(this.uri + "resetbadge.php?",
							"POST", localArrayList)).toString());

			if (localJSONObject.getBoolean("status") == false) {
				localHashMap.put("result", "false");
				localHashMap
						.put("status", localJSONObject.getBoolean("status"));
				localHashMap.put("error", localJSONObject.getString("error"));
				return localHashMap;
			} else if (localJSONObject.getBoolean("status") == true) {
				localHashMap.put("result", "true");

				return localHashMap;
			}

		} catch (Exception ae) {
			Log.e("ae==", "" + ae);
			localHashMap.put("result", "ERROR");
			return localHashMap;

		}
		localHashMap.put("result", "ERROR");
		return localHashMap;

	}
}
