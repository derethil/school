use std::error::Error;
use std::fs;

use clap::Parser;
use colored::*;
use regex::RegexBuilder;

/// ------ ARGUMENT PARSING ------

#[derive(Parser, Debug)]
#[clap(author, version, about, long_about=None)]
pub struct Args {
    /// Pattern to search for
    pub query: String,

    /// File to search
    pub filename: String,

    /// Ignore case distinctions in patterns and data
    #[clap(short, long)]
    pub ignore_case: bool,
}

/// ------ PROGRAM LOGIC ------

pub fn run(args: Args) -> Result<(), Box<dyn Error>> {
    let contents = fs::read_to_string(args.filename)?;

    let results = search(&args.query, &contents, args.ignore_case);

    for line in results {
        println!("{}", line);
    }

    Ok(())
}

pub fn search<'a>(query: &str, contents: &str, ignore_case: bool) -> Vec<String> {
    let mut results = Vec::new();

    for line in contents.lines() {
        let query = RegexBuilder::new(query)
            .case_insensitive(ignore_case)
            .build()
            .expect("Invalid Regex");

        for regex_match in query.find_iter(line) {
            results.push(colorize(&line, regex_match))
        }
    }

    results
}

fn colorize(line: &str, regex_match: regex::Match) -> String {
    let query = regex_match.as_str();
    line.replace(&query, &query.red().bold().to_string())
}

/// ------ TESTING ------

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn ignore_case() {
        let query = "duct";
        let contents = "\
Rust:
safe, fast, productive.
Pick three.
Duct tape.";

        assert_eq!(
            vec![format!("safe, fast, pro{}ive.", "duct".red().bold())],
            search(query, contents, false)
        );
    }

    #[test]
    fn case_insensitive() {
        let query = "rUsT";
        let contents = "\
Rust:
safe, fast, productive.
Pick three.
Trust me.";

        assert_eq!(
            vec![
                format!("{}:", "Rust".red().bold()),
                format!("T{} me.", "rust".red().bold())
            ],
            search(query, contents, true)
        );
    }
}
