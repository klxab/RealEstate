import java.util.logging.*;

/**
 * Main test class for RealEstate project.
 * Demonstrates creation, discounting and price calculations.
 */
public class RealEstateTest {

    /** global logger for entire application */
    private static final Logger LOGGER = Logger.getLogger("realestate");

    static {
        try {
            // configure logging to file
            FileHandler fh = new FileHandler("realEstateApp.log", true); // append = true
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(true); // also print to console
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while setting up logger", e);
        }
    }

    /**
     * Interface that defines general property behavior.
     */
    public interface PropertyInterface {
        void makeDiscount(int percentage);
        int getTotalPrice();
        double averageSqmPerRoom();
        String toString();
    }

    /**
     * Interface specific for “Panel” type objects.
     */
    public interface PanelInterface {
        boolean hasSameAmount(RealEstate other);
        int roomPrice();
    }

    /**
     * Represents a generic RealEstate object.
     */
    public static class RealEstate implements PropertyInterface {

        protected String city;
        protected double price;
        protected int sqm;
        protected double numberOfRooms;
        protected Genre genre;

        /**
         * Types of RealEstate.
         */
        public enum Genre {
            FAMILYHOUSE,
            CONDOMINIUM,
            FARM
        }

        /**
         * Creates new real estate object.
         */
        public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
            LOGGER.info("RealEstate constructor called");
            this.city = city;
            this.price = price;
            this.sqm = sqm;
            this.numberOfRooms = numberOfRooms;
            this.genre = genre;
        }

        /**
         * Reduces price by given percentage.
         */
        @Override
        public void makeDiscount(int percentage) {
            LOGGER.info("makeDiscount(" + percentage + ") called");
            this.price = this.price * (1 - percentage / 100.0);
        }

        /**
         * Calculates total price based on city multiplier.
         */
        @Override
        public int getTotalPrice() {
            LOGGER.info("getTotalPrice() called");
            double total = price * sqm;
            try {
                switch (city) {
                    case "Budapest": total *= 1.3; break;
                    case "Debrecen": total *= 1.2; break;
                    case "Nyíregyháza": total *= 1.15; break;
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in getTotalPrice()", e);
            }
            return (int) total;
        }

        /**
         * Returns average sqm size per room.
         */
        @Override
        public double averageSqmPerRoom() {
            LOGGER.info("averageSqmPerRoom() called");
            return sqm / numberOfRooms;
        }

        @Override
        public String toString() {
            LOGGER.info("toString() called");
            return "RealEstate [city=" + city + ", price per sqm=" + price + ", sqm=" + sqm +
                    ", numberOfRooms=" + numberOfRooms + ", genre=" + genre +
                    ", totalPrice=" + getTotalPrice() +
                    ", avgSqmPerRoom=" + averageSqmPerRoom() + "]";
        }
    }

    /**
     * Represents panel building type real estates.
     */
    public static class Panel extends RealEstate implements PanelInterface {

        private int floor;
        private boolean isInsulated;

        /**
         * Creates a Panel real estate object.
         */
        public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
            super(city, price, sqm, numberOfRooms, genre);
            LOGGER.info("Panel constructor called");
            this.floor = floor;
            this.isInsulated = isInsulated;
        }

        /**
         * Calculates final price including panel logic.
         */
        @Override
        public int getTotalPrice() {
            LOGGER.info("Panel.getTotalPrice() called");
            double total = price * sqm;
            try {
                switch (city) {
                    case "Budapest": total *= 1.3; break;
                    case "Debrecen": total *= 1.2; break;
                    case "Nyíregyháza": total *= 1.15; break;
                }
                if (floor >= 0 && floor <= 2) total *= 1.05;
                if (floor == 10) total *= 0.95;
                if (isInsulated) total *= 1.05;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in Panel.getTotalPrice()", e);
            }
            return (int) total;
        }

        /**
         * Checks if two properties have same total price.
         */
        @Override
        public boolean hasSameAmount(RealEstate other) {
            LOGGER.info("hasSameAmount() called");
            return this.getTotalPrice() == other.getTotalPrice();
        }

        /**
         * Returns price per room.
         */
        @Override
        public int roomPrice() {
            LOGGER.info("roomPrice() called");
            return (int) (price * sqm / numberOfRooms);
        }

        @Override
        public String toString() {
            LOGGER.info("Panel.toString() called");
            return "Panel [city=" + city + ", price per sqm=" + price + ", sqm=" + sqm +
                    ", numberOfRooms=" + numberOfRooms + ", genre=" + genre +
                    ", floor=" + floor + ", insulated=" + isInsulated +
                    ", totalPrice=" + getTotalPrice() +
                    ", avgSqmPerRoom=" + averageSqmPerRoom() + "]";
        }
    }

    /** simple test */
    public static void testRealEstateCreation() {
        LOGGER.info("testRealEstateCreation() called");
        RealEstate house = new RealEstate("Tokaj", 7000, 350, 3, RealEstate.Genre.FARM);
        System.out.println("=== Test 1: RealEstate Creation ===");
        System.out.println(house);
    }

    /** panel test */
    public static void testPanelCreation() {
        LOGGER.info("testPanelCreation() called");
        Panel panel = new Panel("Kalmanhaza", 1500, 60, 3, RealEstate.Genre.CONDOMINIUM, 6, false);
        System.out.println("=== Test 2: Panel Creation ===");
        System.out.println(panel);
    }

    /** discount test */
    public static void testPanelDiscount() {
        LOGGER.info("testPanelDiscount() called");
        Panel panel = new Panel("Tokaj", 7000, 350, 3, RealEstate.Genre.CONDOMINIUM, 3, true);
        panel.makeDiscount(25);
        System.out.println("=== Test 3: Panel Discount ===");
        System.out.println(panel);
        System.out.println("Room price: " + panel.roomPrice());
    }

    /** entry point */
    public static void main(String[] args) {
        LOGGER.info("main() called");
        testRealEstateCreation();
        testPanelCreation();
        testPanelDiscount();
    }
}
