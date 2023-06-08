package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Edge;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT DISTINCT a.`AlbumId`, a.`Title`, sum(t.`Milliseconds`) AS durata "
				+ "FROM album a, track t "
				+ "WHERE a.`AlbumId`=t.`AlbumId` "
				+ "GROUP BY a.`AlbumId`";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title"), res.getDouble("durata")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Album> getVertici(int durata){
		final String sql = "SELECT a.*, sum(t.`Milliseconds`) AS durata_album "
				+ "FROM album a, track t "
				+ "WHERE a.`AlbumId` = t.`AlbumId` "
				+ "GROUP BY a.`AlbumId` "
				+ "HAVING durata_album > ?";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, durata);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title"), res.getDouble("durata_album")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Edge> getEdge(int durata, Map<Integer, Album> mappa){
		final String sql = "SELECT tab1.`AlbumId` AS id1, tab2.`AlbumId` AS id2 "
				+ "FROM (SELECT a.`AlbumId`,p.`PlaylistId` "
				+ "FROM playlisttrack p, album a, track t "
				+ "WHERE p.`TrackId`=t.`TrackId` "
				+ "AND t.`AlbumId`=a.`AlbumId` "
				+ "AND a.`AlbumId` IN (SELECT a1.albumId "
				+ "		FROM (SELECT a.*, sum(t.`Milliseconds`) AS durata_album "
				+ "		FROM album a, track t "
				+ "		WHERE a.`AlbumId` = t.`AlbumId` "
				+ "		GROUP BY a.`AlbumId` "
				+ "		HAVING durata_album > ?) AS a1) "
				+ "GROUP BY a.`AlbumId`,p.`PlaylistId`) AS tab1, "
				+ "(SELECT a.`AlbumId`,p.`PlaylistId` "
				+ "FROM playlisttrack p, album a, track t "
				+ "WHERE p.`TrackId`=t.`TrackId` "
				+ "AND t.`AlbumId`=a.`AlbumId` "
				+ "AND a.`AlbumId` IN (SELECT a1.albumId "
				+ "		FROM (SELECT a.*, sum(t.`Milliseconds`) AS durata_album "
				+ "		FROM album a, track t "
				+ "		WHERE a.`AlbumId` = t.`AlbumId` "
				+ "		GROUP BY a.`AlbumId` "
				+ "		HAVING durata_album > ?) AS a1) "
				+ "GROUP BY a.`AlbumId`,p.`PlaylistId`) AS tab2 "
				+ "WHERE tab1.`PlaylistId`=tab2.`PlaylistId` "
				+ "AND tab1.`AlbumId`<tab2.`AlbumId` "
				+ "GROUP BY tab1.`AlbumId`, tab2.`AlbumId`";
		List<Edge> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, durata);
			st.setInt(2, durata);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Edge(mappa.get(res.getInt("id1")), mappa.get(res.getInt("id2"))));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public Map<Integer, Album> getMappaAlbum() {
		final String sql = "SELECT DISTINCT a.`AlbumId`, a.`Title`, sum(t.`Milliseconds`) AS durata "
				+ "FROM album a, track t "
				+ "WHERE a.`AlbumId`=t.`AlbumId` "
				+ "GROUP BY a.`AlbumId`";
		Map<Integer, Album> result = new HashMap<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.put(res.getInt("AlbumId"), new Album(res.getInt("AlbumId"), res.getString("Title"), res.getDouble("durata")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
}


/*
SELECT tab1.`AlbumId`, tab2.`AlbumId`
FROM (SELECT a.`AlbumId`,p.`PlaylistId`
FROM playlisttrack p, album a, track t
WHERE p.`TrackId`=t.`TrackId`
AND t.`AlbumId`=a.`AlbumId`
AND a.`AlbumId` IN (SELECT a1.albumId
		FROM (SELECT a.*, sum(t.`Milliseconds`) AS durata_album 
		FROM album a, track t
		WHERE a.`AlbumId` = t.`AlbumId` 
		GROUP BY a.`AlbumId` 
		HAVING durata_album > ?) AS a1)
GROUP BY a.`AlbumId`,p.`PlaylistId`) AS tab1,
(SELECT a.`AlbumId`,p.`PlaylistId`
FROM playlisttrack p, album a, track t
WHERE p.`TrackId`=t.`TrackId`
AND t.`AlbumId`=a.`AlbumId`
AND a.`AlbumId` IN (SELECT a1.albumId
		FROM (SELECT a.*, sum(t.`Milliseconds`) AS durata_album 
		FROM album a, track t
		WHERE a.`AlbumId` = t.`AlbumId` 
		GROUP BY a.`AlbumId` 
		HAVING durata_album > ?) AS a1)
GROUP BY a.`AlbumId`,p.`PlaylistId`) AS tab2
WHERE tab1.`PlaylistId`=tab2.`PlaylistId`
AND tab1.`AlbumId`<tab2.`AlbumId`
GROUP BY tab1.`AlbumId`, tab2.`AlbumId`
 */
  
