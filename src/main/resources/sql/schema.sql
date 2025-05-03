-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    modified TIMESTAMP NOT NULL,
    last_login TIMESTAMP,
    token VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

-- Create user_phones table
CREATE TABLE IF NOT EXISTS user_phones (
    user_id UUID NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    city_code VARCHAR(255) NOT NULL,
    country_code VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create index on email for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Create index on token for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_token ON users(token); 