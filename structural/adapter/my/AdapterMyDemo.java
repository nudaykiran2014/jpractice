/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * ADAPTER PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're traveling from INDIA 🇮🇳 to USA 🇺🇸
 * 
 * Your phone charger has an INDIAN PLUG 🔌
 * But USA has DIFFERENT SOCKETS! 🔲
 * 
 * What do you do? Buy a new charger? NO!
 * You use a TRAVEL ADAPTER! 🔄
 * 
 *     Indian Plug 🔌 ──→ [ADAPTER 🔄] ──→ US Socket 🔲
 *     
 * The adapter CONVERTS one interface to another!
 * 
 * THE PATTERN:
 * ─────────────
 *     - You have an EXISTING class (old printer)
 *     - You need a DIFFERENT interface (new system expects)
 *     - ADAPTER makes them work together!
 *     
 *         Old Interface        Adapter         New Interface
 *         ┌──────────┐      ┌──────────┐      ┌──────────┐
 *         │ printOld │ ──→  │ converts │ ──→  │  print   │
 *         └──────────┘      └──────────┘      └──────────┘
 */

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE: Media Player Adapter
// ═══════════════════════════════════════════════════════════════════════════════

// What our app expects (Target Interface)
interface MediaPlayer {
    void play(String filename);
}

// What we have - OLD library that plays only MP3
class LegacyMp3Player {
    public void playMp3(String filename) {
        System.out.println("  🎵 Playing MP3: " + filename);
    }
}

// Another OLD library for video
class LegacyVideoPlayer {
    public void playVideo(String filename) {
        System.out.println("  🎬 Playing Video: " + filename);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// ADAPTERS - Make old libraries work with new interface!
// ═══════════════════════════════════════════════════════════════════════════════

class Mp3Adapter implements MediaPlayer {
    private LegacyMp3Player legacyPlayer;
    
    public Mp3Adapter(LegacyMp3Player legacyPlayer) {
        this.legacyPlayer = legacyPlayer;
    }
    
    public void play(String filename) {
        // Adapt: Convert play() call to playMp3() call
        legacyPlayer.playMp3(filename);
    }
}

class VideoAdapter implements MediaPlayer {
    private LegacyVideoPlayer legacyPlayer;
    
    public VideoAdapter(LegacyVideoPlayer legacyPlayer) {
        this.legacyPlayer = legacyPlayer;
    }
    
    public void play(String filename) {
        // Adapt: Convert play() call to playVideo() call
        legacyPlayer.playVideo(filename);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Payment Gateway Adapter
// ═══════════════════════════════════════════════════════════════════════════════

// What our e-commerce app expects
interface PaymentProcessor {
    void processPayment(double amount);
    boolean refund(double amount);
}

// Old PayPal SDK (different interface)
class PayPalSDK {
    public void sendPayment(double amt) {
        System.out.println("  💳 PayPal: Sent ₹" + amt);
    }
    public void refundPayment(double amt) {
        System.out.println("  💳 PayPal: Refunded ₹" + amt);
    }
}

// Old Stripe SDK (another different interface)
class StripeSDK {
    public void charge(int amountInPaise) {
        System.out.println("  💳 Stripe: Charged " + amountInPaise + " paise");
    }
    public void reverseCharge(int amountInPaise) {
        System.out.println("  💳 Stripe: Reversed " + amountInPaise + " paise");
    }
}

// ADAPTERS
class PayPalAdapter implements PaymentProcessor {
    private PayPalSDK paypal;
    
    public PayPalAdapter(PayPalSDK paypal) {
        this.paypal = paypal;
    }
    
    public void processPayment(double amount) {
        paypal.sendPayment(amount);
    }
    
    public boolean refund(double amount) {
        paypal.refundPayment(amount);
        return true;
    }
}

class StripeAdapter implements PaymentProcessor {
    private StripeSDK stripe;
    
    public StripeAdapter(StripeSDK stripe) {
        this.stripe = stripe;
    }
    
    public void processPayment(double amount) {
        // Convert rupees to paise!
        stripe.charge((int)(amount * 100));
    }
    
    public boolean refund(double amount) {
        stripe.reverseCharge((int)(amount * 100));
        return true;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class AdapterMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ ADAPTER PATTERN - MEDIA PLAYER ═══\n");
        
        // Old legacy players
        LegacyMp3Player oldMp3 = new LegacyMp3Player();
        LegacyVideoPlayer oldVideo = new LegacyVideoPlayer();
        
        // Wrap them with adapters
        MediaPlayer mp3Player = new Mp3Adapter(oldMp3);
        MediaPlayer videoPlayer = new VideoAdapter(oldVideo);
        
        // Now they both work with same interface!
        System.out.println("Using unified MediaPlayer interface:");
        mp3Player.play("song.mp3");
        videoPlayer.play("movie.mp4");
        
        System.out.println("\n═══ ADAPTER PATTERN - PAYMENT GATEWAY ═══\n");
        
        // Old SDKs with different interfaces
        PayPalSDK paypalSdk = new PayPalSDK();
        StripeSDK stripeSdk = new StripeSDK();
        
        // Wrap them with adapters
        PaymentProcessor paypal = new PayPalAdapter(paypalSdk);
        PaymentProcessor stripe = new StripeAdapter(stripeSdk);
        
        // Now they both work with same interface!
        System.out.println("Using unified PaymentProcessor interface:");
        paypal.processPayment(100.0);
        stripe.processPayment(100.0);  // Automatically converts to paise!
        
        System.out.println("\nRefunding:");
        paypal.refund(50.0);
        stripe.refund(50.0);
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT ADAPTER (BAD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Client code knows about EACH library's interface
 *     if (type == "paypal") {
 *         paypalSdk.sendPayment(amount);
 *     } else if (type == "stripe") {
 *         stripeSdk.charge((int)(amount * 100));
 *     } else if (type == "razorpay") {
 *         razorpaySdk.doPayment(amount, "INR");
 *     }
 *     // Adding new payment = change client code!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH ADAPTER (GOOD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Client code uses ONE interface
 *     PaymentProcessor processor = getProcessor(type);
 *     processor.processPayment(amount);
 *     // Adding new payment = just add new adapter!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. TRAVEL ADAPTER
 *        - US plug → Adapter → Indian socket
 *     
 *     2. CARD READER
 *        - SD card → Adapter → USB port
 *     
 *     3. JAVA I/O
 *        - InputStreamReader adapts InputStream to Reader
 *     
 *     4. SPRING
 *        - HandlerAdapter adapts different handler types
 *     
 *     5. JDBC
 *        - JDBC Driver adapts database to Java interface
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * ADAPTER vs FACADE vs DECORATOR:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ADAPTER:   Changes interface (A → B)
 *     FACADE:    Simplifies interface (complex → simple)
 *     DECORATOR: Adds features (same interface, more behavior)
 */
