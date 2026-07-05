package com.bakersin.config;

import com.bakersin.model.Category;
import com.bakersin.model.Product;
import com.bakersin.model.ProductUnit;
import com.bakersin.repository.CategoryRepository;
import com.bakersin.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Seeds the database with 6 categories x 6 products = 36 products on first startup,
 * using auto-generated placehold.co placeholder images so the app runs with zero setup
 * and no image licensing risk. Swap img() calls for real photography (S3/Cloudinary/CDN)
 * before going live.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category cakes = save(new Category("Cakes", "cakes", "#FF6B9D", "🎂"));
        Category pastries = save(new Category("Pastries & Desserts", "pastries-desserts", "#FFA36B", "🍰"));
        Category pizza = save(new Category("Pizza", "pizza", "#FF4B4B", "🍕"));
        Category burgers = save(new Category("Burgers", "burgers", "#FFC93C", "🍔"));
        Category mocktails = save(new Category("Mocktails", "mocktails", "#4BC0FF", "🍹"));
        Category cookies = save(new Category("Cookies & Snacks", "cookies-snacks", "#8B5CF6", "🍪"));

        seedCakes(cakes);
        seedPastries(pastries);
        seedPizza(pizza);
        seedBurgers(burgers);
        seedMocktails(mocktails);
        seedCookies(cookies);
    }

    private void seedCakes(Category c) {
        product(c, "Chocolate Truffle Cake", "Rich layers of chocolate sponge with silky truffle ganache.",
                "649", "549", ProductUnit.PER_KG, 4.8, 20, true);
        product(c, "Red Velvet Cake", "Classic red velvet with a tangy cream-cheese frosting.",
                "699", null, ProductUnit.PER_KG, 4.7, 15, true);
        product(c, "Black Forest Cake", "Cherries, whipped cream, and chocolate shavings.",
                "579", "499", ProductUnit.PER_KG, 4.6, 18, true);
        product(c, "Vanilla Fresh Cream Cake", "Light vanilla sponge with fresh whipped cream.",
                "499", null, ProductUnit.PER_KG, 4.5, 25, true);
        product(c, "Butterscotch Cake", "Caramelised butterscotch crunch through soft sponge.",
                "549", null, ProductUnit.PER_KG, 4.6, 16, true);
        product(c, "Pineapple Cake", "A light, fruity classic with fresh pineapple chunks.",
                "459", "399", ProductUnit.PER_KG, 4.4, 22, true);
    }

    private void seedPastries(Category c) {
        product(c, "Chocolate Éclair", "Choux pastry filled with custard, glazed with chocolate.",
                "89", null, ProductUnit.PER_PIECE, 4.5, 40, true);
        product(c, "Tiramisu Cup", "Espresso-soaked layers with mascarpone cream.",
                "179", "149", ProductUnit.PER_PIECE, 4.8, 20, true);
        product(c, "Fruit Tart", "Buttery tart shell topped with pastry cream and fresh fruit.",
                "149", null, ProductUnit.PER_PIECE, 4.6, 25, true);
        product(c, "Baked Cheesecake Slice", "Dense, creamy New York-style cheesecake.",
                "199", "169", ProductUnit.PER_PIECE, 4.7, 18, true);
        product(c, "Chocolate Mousse Cup", "Airy dark chocolate mousse, lightly sweetened.",
                "129", null, ProductUnit.PER_PIECE, 4.5, 30, true);
        product(c, "Cinnamon Roll", "Soft roll swirled with cinnamon sugar and icing.",
                "99", null, ProductUnit.PER_PIECE, 4.4, 35, true);
    }

    private void seedPizza(Category c) {
        product(c, "Margherita Pizza", "Classic tomato, mozzarella, and fresh basil.",
                "249", null, ProductUnit.PER_BOX, 4.5, 30, true);
        product(c, "Farmhouse Pizza", "Loaded with capsicum, onion, tomato, and mushroom.",
                "329", "279", ProductUnit.PER_BOX, 4.6, 22, true);
        product(c, "Peppy Paneer Pizza", "Spiced paneer, capsicum, and red paprika.",
                "349", null, ProductUnit.PER_BOX, 4.5, 20, true);
        product(c, "Chicken Tikka Pizza", "Smoky chicken tikka chunks with onion and peppers.",
                "399", "349", ProductUnit.PER_BOX, 4.7, 18, false);
        product(c, "Pepperoni Pizza", "Loaded with classic spiced pepperoni slices.",
                "419", null, ProductUnit.PER_BOX, 4.6, 15, false);
        product(c, "Cheese Burst Pizza", "Extra cheese stuffed into the crust for a gooey bite.",
                "379", null, ProductUnit.PER_BOX, 4.8, 20, true);
    }

    private void seedBurgers(Category c) {
        product(c, "Classic Veg Burger", "Crisp veg patty, lettuce, and tangy mayo.",
                "99", null, ProductUnit.PER_PIECE, 4.3, 40, true);
        product(c, "Paneer Tikka Burger", "Grilled paneer tikka patty with mint mayo.",
                "149", "129", ProductUnit.PER_PIECE, 4.5, 30, true);
        product(c, "Classic Chicken Burger", "Juicy grilled chicken patty with fresh veggies.",
                "159", null, ProductUnit.PER_PIECE, 4.6, 28, false);
        product(c, "Crispy Chicken Burger", "Crunchy fried chicken fillet with spicy mayo.",
                "179", "149", ProductUnit.PER_PIECE, 4.7, 25, false);
        product(c, "Double Cheese Burger", "Two patties, double cheese, house sauce.",
                "199", null, ProductUnit.PER_PIECE, 4.6, 20, false);
        product(c, "Mushroom Melt Burger", "Sautéed mushrooms with melted cheese.",
                "169", null, ProductUnit.PER_PIECE, 4.4, 22, true);
    }

    private void seedMocktails(Category c) {
        product(c, "Virgin Mojito", "Lime, mint, and soda over crushed ice.",
                "129", null, ProductUnit.PER_GLASS, 4.5, 50, true);
        product(c, "Blue Lagoon", "Tropical citrus with a vibrant blue curaçao syrup.",
                "149", "129", ProductUnit.PER_GLASS, 4.6, 40, true);
        product(c, "Watermelon Cooler", "Fresh watermelon juice with a hint of mint.",
                "119", null, ProductUnit.PER_GLASS, 4.4, 45, true);
        product(c, "Passion Fruit Fizz", "Tangy passion fruit pulp topped with soda.",
                "159", null, ProductUnit.PER_GLASS, 4.5, 35, true);
        product(c, "Strawberry Basil Smash", "Muddled strawberries and basil over ice.",
                "169", "139", ProductUnit.PER_GLASS, 4.7, 30, true);
        product(c, "Classic Lemonade", "Freshly squeezed lemon, mint, and a touch of soda.",
                "89", null, ProductUnit.PER_GLASS, 4.3, 60, true);
    }

    private void seedCookies(Category c) {
        product(c, "Chocolate Chip Cookies", "Classic chewy cookies loaded with choc chips.",
                "199", null, ProductUnit.PER_BOX, 4.6, 30, true);
        product(c, "Double Chocolate Cookies", "Cocoa dough studded with dark chocolate chunks.",
                "229", "199", ProductUnit.PER_BOX, 4.7, 25, true);
        product(c, "Oatmeal Raisin Cookies", "Hearty oats and sweet raisins in every bite.",
                "189", null, ProductUnit.PER_BOX, 4.3, 20, true);
        product(c, "Butter Cookies Tin", "Melt-in-the-mouth classic butter cookies.",
                "249", null, ProductUnit.PER_BOX, 4.5, 22, true);
        product(c, "Trail Mix Snack Pack", "Nuts, seeds, and dried fruit for on-the-go snacking.",
                "159", "139", ProductUnit.PER_BOX, 4.4, 28, true);
        product(c, "Cheese Crackers", "Crisp, savoury crackers baked with real cheese.",
                "129", null, ProductUnit.PER_BOX, 4.2, 35, true);
    }

    private void product(Category category, String name, String description,
                          String price, String salePrice, ProductUnit unit,
                          double rating, int stock, boolean veg) {
        Product p = new Product();
        p.setCategory(category);
        p.setName(name);
        p.setSlug(slugify(category.getSlug(), name));
        p.setDescription(description);
        p.setPrice(new BigDecimal(price));
        p.setSalePrice(salePrice != null ? new BigDecimal(salePrice) : null);
        p.setUnit(unit);
        p.setImageUrl(img(category.getAccentColor(), name));
        p.setRating(rating);
        p.setStock(stock);
        p.setVeg(veg);
        productRepository.save(p);
    }

    private Category save(Category category) {
        return categoryRepository.save(category);
    }

    private String slugify(String categorySlug, String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
        return categorySlug + "-" + base;
    }

    /** Colour-coded placehold.co image labelled with the product name — zero setup, no licensing risk. */
    private String img(String accentColorHex, String label) {
        String hex = accentColorHex.replace("#", "");
        String encodedLabel;
        try {
            encodedLabel = URLEncoder.encode(label, StandardCharsets.UTF_8.toString()).replace("+", "+");
        } catch (UnsupportedEncodingException e) {
            encodedLabel = label.replace(" ", "+");
        }
        return "https://placehold.co/400x300/" + hex + "/FFFFFF?text=" + encodedLabel;
    }
}
