module.exports = {
    preset: "ts-jest",
    testEnvironment: "jsdom",
    moduleFileExtensions: ["js", "jsx", "json", "tsx", "ts"],
    transform: {
        ".+\\.css$": "esbuild-jest",
        "^.+\\.tsx?$": "esbuild-jest"
    },
    moduleNameMapper: {
        "\\.(css|less|sass|scss)$": "<rootDir>/__mocks__/styleMock.js",
        "\\.(gif|ttf|eot|svg)$": "<rootDir>/__mocks__/fileMock.js",
    },
    transformIgnorePatterns: ["<rootDir>/node_modules"],
    watchPathIgnorePatterns: ["<rootDir>/dist", "<rootDir>/node_modules"]
};