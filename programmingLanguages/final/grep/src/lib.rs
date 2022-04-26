use clap::Parser;

use std::error::Error;
use std::fs;

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

    let results = search(&args.query, &contents, &args.ignore_case);

    for line in results {
        println!("{}", line);
    }

    Ok(())
}

pub fn search<'a>(query: &str, contents: &'a str, ignore_case: &bool) -> Vec<&'a str> {
    let mut results = Vec::new();

    for line in contents.lines() {
        let lowercase_line = line.to_lowercase();

        let line_query = if *ignore_case {
            lowercase_line.as_str()
        } else {
            line
        };

        if line_query.contains(&query.to_lowercase()) {
            results.push(line);
        }
    }

    results
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
            vec!["safe, fast, productive."],
            search(query, contents, &false)
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

        assert_eq!(vec!["Rust:", "Trust me."], search(query, contents, &true));
    }
}
