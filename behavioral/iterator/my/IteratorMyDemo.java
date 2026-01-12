/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * ITERATOR PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you have a BOX OF CHOCOLATES 🍫📦
 * 
 * You want to eat them ONE BY ONE without knowing:
 *   - How many chocolates are inside
 *   - How they're arranged (stack? row? random?)
 * 
 * You just ask: "Give me NEXT chocolate" 🍫
 *               "Any MORE chocolates left?" ✅/❌
 * 
 * THAT'S IT! That's the Iterator pattern!
 * 
 * THE PATTERN:
 * ─────────────
 *   - hasNext() → "Is there more?"
 *   - next()    → "Give me the next one"
 *   
 *   You don't care HOW items are stored (Array? List? Tree?)
 *   You just iterate through them the SAME WAY!
 * 
 *       📦 Collection          🔄 Iterator
 *     ┌─────────────┐      ┌──────────────┐
 *     │ 🍫 🍫 🍫 🍫  │ ──→  │ hasNext()    │
 *     │ (any order) │      │ next()       │
 *     └─────────────┘      └──────────────┘
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Iterator Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface MyIterator<T> {
    boolean hasNext();
    T next();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Collection Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface MyCollection<T> {
    MyIterator<T> createIterator();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Concrete Collection - Playlist 🎵
// ═══════════════════════════════════════════════════════════════════════════════
class Song {
    private String name;
    private String artist;
    
    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }
    
    public String toString() {
        return "🎵 " + name + " - " + artist;
    }
}

class Playlist implements MyCollection<Song> {
    private List<Song> songs = new ArrayList<>();
    
    public void addSong(Song song) {
        songs.add(song);
    }
    
    // Return iterator to go through songs
    public MyIterator<Song> createIterator() {
        return new PlaylistIterator(songs);
    }
    
    // Can also have reverse iterator!
    public MyIterator<Song> createReverseIterator() {
        return new ReversePlaylistIterator(songs);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 4: Concrete Iterators
// ═══════════════════════════════════════════════════════════════════════════════

// Forward Iterator (normal order)
class PlaylistIterator implements MyIterator<Song> {
    private List<Song> songs;
    private int position = 0;
    
    public PlaylistIterator(List<Song> songs) {
        this.songs = songs;
    }
    
    public boolean hasNext() {
        return position < songs.size();
    }
    
    public Song next() {
        return songs.get(position++);
    }
}

// Reverse Iterator (backwards!)
class ReversePlaylistIterator implements MyIterator<Song> {
    private List<Song> songs;
    private int position;
    
    public ReversePlaylistIterator(List<Song> songs) {
        this.songs = songs;
        this.position = songs.size() - 1;
    }
    
    public boolean hasNext() {
        return position >= 0;
    }
    
    public Song next() {
        return songs.get(position--);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class IteratorMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ ITERATOR PATTERN - PLAYLIST ═══\n");
        
        // Create playlist
        Playlist playlist = new Playlist();
        playlist.addSong(new Song("Shape of You", "Ed Sheeran"));
        playlist.addSong(new Song("Blinding Lights", "The Weeknd"));
        playlist.addSong(new Song("Dance Monkey", "Tones and I"));
        playlist.addSong(new Song("Someone Like You", "Adele"));
        
        // Play in normal order
        System.out.println("▶️ Playing in ORDER:");
        MyIterator<Song> iterator = playlist.createIterator();
        while (iterator.hasNext()) {
            System.out.println("  Now playing: " + iterator.next());
        }
        
        System.out.println();
        
        // Play in reverse order
        System.out.println("◀️ Playing in REVERSE:");
        MyIterator<Song> reverseIterator = playlist.createReverseIterator();
        while (reverseIterator.hasNext()) {
            System.out.println("  Now playing: " + reverseIterator.next());
        }
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT ITERATOR (BAD - Exposed internals):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class Playlist {
 *         public List<Song> songs;  // Exposed!
 *     }
 *     
 *     // Client code depends on internal structure
 *     for (int i = 0; i < playlist.songs.size(); i++) {
 *         Song s = playlist.songs.get(i);
 *     }
 *     
 *     Problems:
 *     - Client knows it's a List (what if we change to array?)
 *     - Client knows how to traverse (what if order changes?)
 *     - Can't easily add different traversal methods
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH ITERATOR (GOOD - Hidden internals):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Client doesn't know/care about internal structure
 *     Iterator<Song> it = playlist.createIterator();
 *     while (it.hasNext()) {
 *         Song s = it.next();
 *     }
 *     
 *     Benefits:
 *     - Collection internals are HIDDEN
 *     - Can change internal structure without affecting client
 *     - Can have MULTIPLE iterators (forward, reverse, filtered)
 *     - Same interface for ANY collection type
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * JAVA'S BUILT-IN ITERATOR:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     List<String> list = Arrays.asList("A", "B", "C");
 *     
 *     // Using Iterator
 *     Iterator<String> it = list.iterator();
 *     while (it.hasNext()) {
 *         System.out.println(it.next());
 *     }
 *     
 *     // For-each loop (uses Iterator internally!)
 *     for (String s : list) {
 *         System.out.println(s);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. NETFLIX / SPOTIFY
 *        - Next episode, Next song
 *        - Don't know how library is stored
 *     
 *     2. SOCIAL MEDIA FEED
 *        - Scroll through posts one by one
 *        - Don't know if it's from database, cache, or API
 *     
 *     3. FILE SYSTEM
 *        - Iterate through files in folder
 *        - Works same for local, cloud, network drives
 *     
 *     4. DATABASE RESULT SET
 *        - resultSet.next() to get next row
 *        - Don't know internal cursor implementation
 *     
 *     5. JAVA COLLECTIONS
 *        - ArrayList, LinkedList, HashSet all have iterator()
 *        - Same way to traverse ALL of them!
 */
