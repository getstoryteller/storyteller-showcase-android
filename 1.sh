#!/usr/bin/env bash
          # Fetch the latest tag
          LATEST_TAG=$(git describe --tags --abbrev=0)

          # Generate release notes for non-merge commits including author's email and commit message
          COMMIT_LOGS=$(git log $LATEST_TAG..HEAD --no-merges --pretty=format:"%H|%ae|%s")

          IFS=$'\n'
          ADDITIONAL_COMMIT_NOTES="### Additional Commits:\n"
          for line in $COMMIT_LOGS; do
            IFS='|' read -ra COMMIT <<< "$line"
            HASH=${COMMIT[0]}
            AUTHOR_EMAIL=${COMMIT[1]}
            MESSAGE=${COMMIT[2]}
            # Search for the first commit on GitHub by this email to get the username
            USERNAME=$(curl -s "https://api.github.com/search/commits?q=author-email:$AUTHOR_EMAIL" \
              | jq -r '.items[0].author.login')

            # If no username found, use the email
            USERNAME=${USERNAME:-$AUTHOR_EMAIL}

            echo "Username: $USERNAME"
            SHORT_HASH=$(echo "$HASH" | cut -c1-7)
            # Append to commit notes
            ADDITIONAL_COMMIT_NOTES+="- $MESSAGE (#$SHORT_HASH) by @$USERNAME \n"
            echo "- $MESSAGE (#$SHORT_HASH) by @$USERNAME"
          done
