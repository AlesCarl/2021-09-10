package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Recensioni;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	
	public List<Business> getAllBusinessCitta(String citta){
		String sql = "SELECT b.* "
				+ "FROM Business b "
				+ "where b.`city`= ? ";
		// Ahwatukee
		
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAlCities(){
		String sql = "SELECT distinct b.`city` "
				+ "FROM Business b "
				+ "order by b.`city` asc ";
		
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
			
				String s=res.getString("city"); 	
				result.add(s);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public LatLng getLatLang(Business bb){
		 
		String sql = "SELECT b.`latitude`, b.`longitude` "
				+ "FROM Business b  "
				+ "where b.`business_id`= ?  ";  
		
		LatLng p1 = null; 
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, bb.getBusinessId());
			ResultSet res = st.executeQuery();
			
			res.first();
			p1= new LatLng(res.getDouble("b.latitude"),res.getDouble("b.longitude"));
			
			res.close();
			st.close();
			conn.close();
			return p1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		 
	 }
	 //return all recensioni  medie dei ristoranti 
	 public Map <String,Double> getAllAvgStar(){
		 
			String sql = "SELECT r.`business_id`, avg(b.stars) as avgStar "
					+ "FROM Business b ,  Reviews r "
					+ "where  b.`business_id`= r.`business_id` "
					+ "group by r.`business_id` "; 
			
			 Map <String,Double> result = new HashMap<>();
			Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				
				ResultSet res = st.executeQuery();
				
				while (res.next()) {
					

					result.put(res.getString("r.business_id"),res.getDouble("avgStar")); 
				
				}
				res.close();
				st.close();
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		

	
	
}
