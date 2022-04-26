use std::process;

use clap::Parser;

fn main() {
    let args = grep::Args::parse();

    if let Err(e) = grep::run(args) {
        eprintln!("Application error: {}", e);
        process::exit(1);
    }
}
