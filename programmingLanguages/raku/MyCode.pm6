use v6;
unit module MyCode;

sub getTokens ($program) is export {
   # Recursively adds values from $text into token list
  # Returns an array of tokens
  sub generateTokens($text, @resultsList) {
    my $newString = "";
    if substr($text, 0..0) ~~ m/ "#" / {
      if $text ~~ m/ "\n" / {

        $newString = $/.postmatch;
      }
    }
    elsif substr($text, 0..0) ~~ m/ "\"" / {
      if substr($text, 1..$text.chars) ~~ m/ "\"" / {
        @resultsList.push("STRING: " ~ $/ ~ $/.prematch ~ $/);
        $newString = $/.postmatch;
      }
    }
    elsif substr($text, 0..0) ~~ m/(<:L>) / {
      if $text ~~ m/ (<:!L>) / {
        @resultsList.push("IDENTIFIER: " ~ $/.prematch);
        $newString = $/ ~ $/.postmatch;
      }
    }
    elsif substr($text, 0..0) ~~ m/ "(" / {
      @resultsList.push("LPAREN: " ~ "(");
      $newString = substr($text, 1..$text.chars);
    }
    elsif substr($text, 0..0) ~~ m/ ")" / {
      @resultsList.push("RPAREN: " ~ ")");
      $newString = substr($text, 1..$text.chars);
    }
    elsif substr($text, 0..0) ~~ m/(<:N>) / {
      if $text ~~ m/ (<:!N>) / {
        @resultsList.push("INTEGER: " ~ $/.prematch);
        $newString = $/ ~ $/.postmatch;
      }
    }
    elsif substr($text, 0..0) ~~ m/ "+" / {
      @resultsList.push("ADDITION: " ~ substr($text, 0..0));
      $newString = substr($text, 1..$text.chars);
    }
    elsif substr($text, 0..0) ~~ m/ "*" / {
      @resultsList.push("MULTIPLICATION: " ~ substr($text, 0..0));
      $newString = substr($text, 1..$text.chars);
    }

    #Recursion ends when $text is empty
    if $newString.chars > 0 {
    return generateTokens($newString.trim, @resultsList);
    }
    else {
      return @resultsList;
    }
  }

  my @results = "";

  my @newData = generateTokens($program.trim, @results);

  return @newData;
}

sub balance (@tokens) is export {
  my $indentLevel = 0;
  for @tokens -> $element {
    my $token = getTokenCharacter($element);

    given $token {
      when " (" {
        $indentLevel++;
      }
      when " )" {
        $indentLevel--;
      }
    }
  }

  return $indentLevel eq 0;
}

sub format (@tokens) is export {
  # stores indent level (i.e., how many spaces at beginning of indent level)
  my $indentLevel = 0;

  # stores whether this is first token on line
  my $firstTokenOnLine = True;

  # stores whether last character was open parentheses
  # (whether to add a space at beginning of this char or not)
  my $wasLastOpenParen = False;

  # return value at end of pretty print
  my $returnValue = "";

  # loop through every element in data and pretty-print
  for @tokens -> $element {
      # get just the token of the data
      my $elementToken = getTokenCharacter($element);

      # variables that we'll flag in order to control indent levels and
      # new lines
      my $decrementThisLine = False;
      my $indentNextLine = False;

      # this will hold what text needs to be added
      my $whatToAdd = "";

      # switch-case based on first character of token
      given $elementToken {
          # most cases will be the same: add a space
          # before the token and just print the token
          # special cases:

          when " )" {
              $whatToAdd = " )";
              $decrementThisLine = True;

          }
          when " (" {
              $whatToAdd = " (";
              $indentNextLine = True;
          }
          # default is just add a space and the element token
          default {
              if !$firstTokenOnLine and !$wasLastOpenParen {
                  $whatToAdd = " ";
              }
              $whatToAdd = $whatToAdd ~ $elementToken;
          }
      }

      # if removing indent, then decrease indent level
      if $decrementThisLine {
          $indentLevel = $indentLevel - 4;
          if $indentLevel < 0 {
              $indentLevel = 0;
          }
      }

      # indent if this is first token on line
      if $firstTokenOnLine {
          # create the indent
          my $indent = "";
          for 1..$indentLevel {
              $indent = $indent ~ " ";
          }
          $returnValue = $returnValue ~ $indent;
      }

      # add new line
      $whatToAdd = $whatToAdd ~ "\n";


      # if increasing indent, then increase indent level
      if $indentNextLine {
          $indentLevel = $indentLevel + 4;
      }

      # check whether this character is an open parentheses
      # this is for next time, whether to add a space or not
      $wasLastOpenParen = $elementToken eq "(";

      # add our construction to return value
      $returnValue = $returnValue ~ $whatToAdd;
  }

  # return the big concatenated string
  return $returnValue;
}

sub getTokenCharacter($data) is export {
    # split the data based on colon
    # colon is used in the Tokenize output
    my @dataSplit = split(/\:/, $data, :skip-empty, :v);

    my $returnValue = "";

    # use this to track once we've found the colon
    my $foundColon = False;

    for @dataSplit -> $element {
        if $foundColon == True {
            # after finding first colon, append all data to return value
            $returnValue = $returnValue ~ $element;
        }
        elsif $element eq ":" {
            # once we find first colon, mark everything after that as relevant
            $foundColon = True;
        }
        else {
            # do nothing before finding first colon
        }
    }
    return $returnValue;
}
