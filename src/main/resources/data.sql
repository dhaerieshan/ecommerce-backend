-- ============================================================
--  SAMPLE DATA - E-Commerce Platform (PostgreSQL)
--  src/main/resources/data.sql
--  Passwords are BCrypt encoded - all passwords are "password@123"
-- ============================================================

-- ─────────────────────────────────────────────────────────────
-- 1. ROLES
-- ─────────────────────────────────────────────────────────────
INSERT INTO roles (name) VALUES ('ADMIN')  ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('VENDOR') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('USER')   ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 2. USERS  (all passwords = "password@123")
-- ─────────────────────────────────────────────────────────────
INSERT INTO users ( username, email, password, first_name, last_name, address) VALUES
( 'admin',        'admin@borneo.com',  '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Admin',  'User',   '1 Admin Street, Chennai'),
( 'john_vendor',  'john@borneo.com',   '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'John',   'Vendor', '22 Vendor Lane, Mumbai'),
( 'priya_user',   'priya@example.com', '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Priya',  'Sharma', '45 MG Road, Bangalore'),
( 'rahul_user',   'rahul@example.com', '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Rahul',  'Kumar',  '12 Anna Nagar, Chennai'),
( 'sneha_user',   'sneha@example.com', '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Sneha',  'Patel',  '78 Bandra West, Mumbai'),
( 'arjun_user',   'arjun@example.com', '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Arjun',  'Nair',   '33 Koramangala, Bangalore'),
( 'meena_vendor', 'meena@borneo.com',  '$2a$12$hDURxM9cyMIcWutFs5ZwOOMuwzYwSP2FcgSHVLM5rIonwaoqaCMOC', 'Meena',  'Vendor', '5 Vendor Park, Delhi')
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 3. USER ROLES
-- ─────────────────────────────────────────────────────────────
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM (VALUES
    (1, 'ADMIN'),
    (2, 'VENDOR'),
    (3, 'USER'),
    (4, 'USER'),
    (5, 'USER'),
    (6, 'USER'),
    (7, 'VENDOR')
) AS mapping(uid, rname)
JOIN users u ON u.id   = mapping.uid::bigint
JOIN roles r ON r.name = mapping.rname
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 4. CATEGORIES (Parent)
-- ─────────────────────────────────────────────────────────────
INSERT INTO categories (id, name, image_path, banner_path, parent_id) VALUES
(1, 'Electronics',      '/images/electronics.jpg',   '/images/electronics-banner.jpg',   NULL),
(2, 'Fashion',          '/images/fashion.jpg',        '/images/fashion-banner.jpg',        NULL),
(3, 'Home & Kitchen',   '/images/home-kitchen.jpg',   '/images/home-kitchen-banner.jpg',   NULL),
(4, 'Sports & Fitness', '/images/sports.jpg',         '/images/sports-banner.jpg',         NULL),
(5, 'Books',            '/images/books.jpg',          '/images/books-banner.jpg',          NULL)
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 5. SUBCATEGORIES
-- ─────────────────────────────────────────────────────────────
INSERT INTO categories (id, name, image_path, banner_path, parent_id) VALUES
(6,  'Smartphones',        '/images/smartphones.jpg', '/images/smartphones-banner.jpg', 1),
(7,  'Laptops',            '/images/laptops.jpg',     '/images/laptops-banner.jpg',     1),
(8,  'Audio',              '/images/audio.jpg',       '/images/audio-banner.jpg',       1),
(9,  'Men''s Clothing',    '/images/mens.jpg',        '/images/mens-banner.jpg',        2),
(10, 'Women''s Clothing',  '/images/womens.jpg',      '/images/womens-banner.jpg',      2),
(11, 'Footwear',           '/images/footwear.jpg',    '/images/footwear-banner.jpg',    2),
(12, 'Kitchen Appliances', '/images/kitchen.jpg',     '/images/kitchen-banner.jpg',     3),
(13, 'Furniture',          '/images/furniture.jpg',   '/images/furniture-banner.jpg',   3),
(14, 'Gym Equipment',      '/images/gym.jpg',         '/images/gym-banner.jpg',         4),
(15, 'Cricket',            '/images/cricket.jpg',     '/images/cricket-banner.jpg',     4)
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 6. PRODUCTS
-- ─────────────────────────────────────────────────────────────
INSERT INTO products (id, name, description, price, stock, image_path, category_id) VALUES
(1,  'Samsung Galaxy S24',           'Latest Samsung flagship with 200MP camera and AI features',            89999,  45,  '/images/products/samsung-s24.jpg',        6),
(2,  'iPhone 15 Pro',                'Apple iPhone 15 Pro with titanium design and A17 chip',                134999, 30,  '/images/products/iphone-15-pro.jpg',      6),
(3,  'OnePlus 12',                   'Flagship killer with Snapdragon 8 Gen 3 and 100W fast charging',       64999,  60,  '/images/products/oneplus-12.jpg',         6),
(4,  'Redmi Note 13 Pro',            'Mid-range powerhouse with 200MP camera and AMOLED display',            24999,  120, '/images/products/redmi-note-13.jpg',      6),
(5,  'MacBook Air M3',               'Apple MacBook Air with M3 chip, 18-hour battery life',                 114999, 20,  '/images/products/macbook-air-m3.jpg',     7),
(6,  'Dell XPS 15',                  'Premium Windows laptop with OLED display and Intel Core i9',           159999, 15,  '/images/products/dell-xps-15.jpg',        7),
(7,  'Lenovo IdeaPad Slim 5',        'Everyday laptop with AMD Ryzen 7 and 16GB RAM',                        72999,  35,  '/images/products/lenovo-ideapad.jpg',     7),
(8,  'ASUS ROG Strix G16',           'Gaming laptop with RTX 4070 and 165Hz display',                       144999, 18,  '/images/products/asus-rog.jpg',           7),
(9,  'Sony WH-1000XM5',              'Industry-leading noise cancelling headphones with 30-hour battery',    29999,  55,  '/images/products/sony-wh1000xm5.jpg',    8),
(10, 'Apple AirPods Pro 2',          'Active noise cancellation with Adaptive Transparency and H2 chip',     24999,  80,  '/images/products/airpods-pro-2.jpg',      8),
(11, 'JBL Charge 5',                 'Waterproof portable Bluetooth speaker with 20-hour playtime',          14999,  90,  '/images/products/jbl-charge-5.jpg',       8),
(12, 'Levi''s 511 Slim Jeans',       'Classic slim fit jeans in dark wash denim',                            3999,   200, '/images/products/levis-511.jpg',          9),
(13, 'Nike Dri-FIT T-Shirt',         'Moisture-wicking athletic t-shirt for training and everyday wear',     1499,   300, '/images/products/nike-dri-fit.jpg',       9),
(14, 'Allen Solly Formal Shirt',     'Slim fit formal shirt perfect for office wear',                        1999,   150, '/images/products/allen-solly.jpg',        9),
(15, 'Fabindia Kurta Set',           'Elegant cotton kurta set with dupatta, perfect for festive occasions', 3499,   100, '/images/products/fabindia-kurta.jpg',     10),
(16, 'Zara Floral Dress',            'Flowy floral midi dress for casual and semi-formal occasions',          4999,   80,  '/images/products/zara-dress.jpg',         10),
(17, 'Nike Air Max 270',             'Lifestyle sneaker with large Air unit for all-day comfort',             12999,  70,  '/images/products/nike-air-max.jpg',       11),
(18, 'Adidas Ultraboost 22',         'Premium running shoe with BOOST midsole for energy return',            15999,  50,  '/images/products/adidas-ultraboost.jpg',  11),
(19, 'Red Tape Formal Shoes',        'Genuine leather formal shoes for office and occasions',                 2999,   120, '/images/products/red-tape-formal.jpg',    11),
(20, 'Instant Pot Duo 7-in-1',       'Electric pressure cooker, slow cooker, rice cooker and more',          8999,   40,  '/images/products/instant-pot.jpg',        12),
(21, 'Philips Air Fryer HD9200',     'Digital air fryer with 7 preset programs and 4.1L capacity',           7499,   55,  '/images/products/philips-airfryer.jpg',   12),
(22, 'Morphy Richards OTG 40L',      'Oven Toaster Griller with convection and 5 heating modes',             5999,   30,  '/images/products/morphy-otg.jpg',         12),
(23, 'IKEA KALLAX Shelf Unit',       'Versatile shelf unit that works as a room divider or bookcase',        12999,  25,  '/images/products/ikea-kallax.jpg',        13),
(24, 'Wakefit Orthopedic Mattress',  'Medium firm memory foam mattress with 10-year warranty',               18999,  20,  '/images/products/wakefit-mattress.jpg',   13),
(25, 'Decathlon 20kg Dumbbell Set',  'Adjustable dumbbell set with rack, suitable for home gym',             4999,   35,  '/images/products/decathlon-dumbbell.jpg', 14),
(26, 'Boldfit Yoga Mat',             'Anti-slip 6mm thick yoga mat with carrying strap',                     799,    200, '/images/products/boldfit-yoga.jpg',       14),
(27, 'SG Cricket Bat English Willow','Full size English willow cricket bat, Grade 1',                        3999,   40,  '/images/products/sg-bat.jpg',             15),
(28, 'Kookaburra Cricket Ball',      'Official match cricket ball, red leather',                              1299,   100, '/images/products/kookaburra-ball.jpg',    15),
(29, 'Clean Code - Robert Martin',   'A handbook of agile software craftsmanship',                            599,    500, '/images/products/clean-code.jpg',         5),
(30, 'Atomic Habits - James Clear',  'An easy and proven way to build good habits and break bad ones',        399,    500, '/images/products/atomic-habits.jpg',      5)
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 7. CARTS
--    Cart PK is user_id (@MapsId) — no separate id column
-- ─────────────────────────────────────────────────────────────
INSERT INTO carts (user_id) VALUES
(3),
(4),
(5)
ON CONFLICT (user_id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 8. CART ITEMS
--    cart_id references carts.user_id
-- ─────────────────────────────────────────────────────────────
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(3, 1,  1),
(3, 9,  1),
(3, 29, 2),
(4, 5,  1),
(4, 17, 1),
(5, 15, 2),
(5, 26, 1)
ON CONFLICT DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 9. ORDERS
-- ─────────────────────────────────────────────────────────────
INSERT INTO orders (id, display_order_number, order_date, user_id, total_amount, status) VALUES
(1, 'ORD-2026-00001', '2026-01-15 10:30:00', 3, 119998.00, 'DELIVERED'),
(2, 'ORD-2026-00002', '2026-01-20 14:00:00', 4, 72999.00,  'SHIPPED'),
(3, 'ORD-2026-00003', '2026-02-01 09:15:00', 5, 8997.00,   'DELIVERED'),
(4, 'ORD-2026-00004', '2026-02-10 16:45:00', 6, 16798.00,  'CONFIRMED'),
(5, 'ORD-2026-00005', '2026-02-20 11:00:00', 3, 24999.00,  'PENDING'),
(6, 'ORD-2026-00006', '2026-02-25 13:30:00', 4, 3998.00,   'PENDING')
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 10. ORDER ITEMS
-- ─────────────────────────────────────────────────────────────
INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(1,  1, 2,  1, 134999.00),
(2,  1, 11, 1, 14999.00),
(3,  2, 7,  1, 72999.00),
(4,  3, 15, 2, 3499.00),
(5,  3, 26, 1, 799.00),
(6,  4, 17, 1, 12999.00),
(7,  4, 18, 1, 15999.00),
(8,  5, 10, 1, 24999.00),
(9,  6, 12, 1, 3999.00),
(10, 6, 13, 1, 1499.00)
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 11. WISHLISTS
-- ─────────────────────────────────────────────────────────────
INSERT INTO wishlists (id, user_id, product_id, date_added) VALUES
(1, 3, 5,  '2026-02-01 10:00:00'),
(2, 3, 8,  '2026-02-05 11:00:00'),
(3, 4, 1,  '2026-02-10 09:00:00'),
(4, 4, 9,  '2026-02-12 14:00:00'),
(5, 5, 16, '2026-02-15 16:00:00'),
(6, 5, 17, '2026-02-18 10:00:00'),
(7, 6, 25, '2026-02-20 12:00:00'),
(8, 6, 27, '2026-02-22 15:00:00')
ON CONFLICT (id) DO NOTHING;

-- ─────────────────────────────────────────────────────────────
-- 12. RESET SEQUENCES (so future JPA saves don't conflict)
-- ─────────────────────────────────────────────────────────────
SELECT setval(pg_get_serial_sequence('users',       'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('roles',       'id'), (SELECT MAX(id) FROM roles));
SELECT setval(pg_get_serial_sequence('categories',  'id'), (SELECT MAX(id) FROM categories));
SELECT setval(pg_get_serial_sequence('products',    'id'), (SELECT MAX(id) FROM products));
SELECT setval(pg_get_serial_sequence('cart_items',  'id'), (SELECT MAX(id) FROM cart_items));
SELECT setval(pg_get_serial_sequence('orders',      'id'), (SELECT MAX(id) FROM orders));
SELECT setval(pg_get_serial_sequence('order_items', 'id'), (SELECT MAX(id) FROM order_items));
SELECT setval(pg_get_serial_sequence('wishlists',   'id'), (SELECT MAX(id) FROM wishlists));
