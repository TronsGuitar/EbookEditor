#!/usr/bin/env python3
"""
create_issues.py – Creates the 13 implementation checklist issues for the
eBookEditor Android app on GitHub.

Usage:
    python3 create_issues.py --token <YOUR_GITHUB_TOKEN> \
                             --repo TronsGuitar/EbookEditor

Requirements:
    pip install requests
"""

import argparse
from datetime import datetime, timezone
from email.utils import parsedate_to_datetime
import sys
import time

try:
    import requests
except ImportError:
    sys.exit("Install the 'requests' package first:  pip install requests")

REQUEST_TIMEOUT_SECONDS = 15
MAX_RETRIES = 3
MIN_RETRY_DELAY_SECONDS = 1


def _retry_backoff_seconds(attempt: int) -> int:
    return 2 ** (attempt - 1)


def _retry_after_seconds(retry_after_value: str | None, fallback_seconds: int) -> int:
    if retry_after_value is None:
        return fallback_seconds

    if retry_after_value.isdigit():
        return int(retry_after_value)

    try:
        retry_after_datetime = parsedate_to_datetime(retry_after_value)
    except (TypeError, ValueError):
        return fallback_seconds

    if retry_after_datetime.tzinfo is None:
        retry_after_datetime = retry_after_datetime.replace(tzinfo=timezone.utc)

    seconds_until_retry = int(
        (retry_after_datetime - datetime.now(timezone.utc)).total_seconds()
    )
    return max(seconds_until_retry, fallback_seconds)

ISSUES = [
    {
        "title": "1. Project Foundation and Structure",
        "body": """## Overview
Set up the foundational project structure for the eBookEditor Android app.

## Tasks
- [ ] Set up project modules for UI, domain, data, and services (repository pattern recommended)
- [ ] Define core Gradle dependencies (Room, Retrofit, AndroidX, Jetpack Compose, etc.)
- [ ] Establish testing frameworks (JUnit, Espresso, MockK)

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "2. Core Data Models",
        "body": """## Overview
Define the core data models used throughout the eBookEditor app.

## Tasks
- [ ] Define models: Project, Chapter, Section, Metadata, AuthorProfile, ComplianceReport, ImportLog, ExportJob
- [ ] Map models to Room entities for local storage

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "3. CRUD and Persistence",
        "body": """## Overview
Implement local data persistence using Room and file storage.

## Tasks
- [ ] Implement Room DAOs/repositories for project and manuscript CRUD
- [ ] Add local file storage and version history for manuscripts, cover images, and import/export files

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "4. Import Features",
        "body": """## Overview
Implement document import capabilities (DOCX, PDF) using Android's Storage Access Framework.

## Tasks
- [ ] Integrate file picker for DOCX/PDF using SAF or system intents
- [ ] Add file upload and storage logic
- [ ] Implement DOCX parsing (use third-party libraries as needed, e.g., Apache POI)
- [ ] Implement PDF parsing for reflowable text (PDFBox/MuPDF/libPdfium)
- [ ] Normalize imported content and log all transformations

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "5. Semantic Content Model",
        "body": """## Overview
Build the semantic document hierarchy in the domain layer.

## Tasks
- [ ] Build the chapter/section/paragraph hierarchy in domain layer
- [ ] Ensure semantic styles take precedence over manual formatting

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "6. Editor Backend",
        "body": """## Overview
Implement the editor backend including autosave, sync, and manuscript management.

## Tasks
- [ ] Implement autosave and real-time sync logic (ViewModel, LiveData/StateFlow)
- [ ] Add chapter/section management (CRUD, reorder)
- [ ] Track word counts and editor progress

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "7. Metadata and Compliance",
        "body": """## Overview
Add metadata management and Draft2Digital compliance validation.

## Tasks
- [ ] Add metadata forms and persistence (title, keywords, AI disclosure, etc.)
- [ ] Implement compliance validator for D2D guidelines (AI use, metadata, image resolution, keyword stuffing)

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "8. Author Profile & Endmatter",
        "body": """## Overview
Implement author profile storage and automatic endmatter generation.

## Tasks
- [ ] Implement author profile data storage
- [ ] Generate Endmatter (About the Author, Also By) from profile

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "9. Export Pipeline",
        "body": """## Overview
Implement the export pipeline for EPUB, Print-Ready PDF, and DOCX formats.

## Tasks
- [ ] Add export logic for EPUB (priority), Print-Ready PDF, and DOCX
- [ ] Validate exports for D2D compliance and log export jobs

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "10. Quality & AI Insights",
        "body": """## Overview
Integrate quality checking and optional AI-powered analysis services.

## Tasks
- [ ] Integrate Quality Check/Auditor (semantic heading order, empty sections, duplicate titles, metadata)
- [ ] Add hooks for optional AI services (character arc, structural/semantic feedback)

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "11. UI Integration & Navigation",
        "body": """## Overview
Connect the backend to the Jetpack Compose UI and implement adaptive navigation patterns.

## Tasks
- [ ] Connect backend to UI (Jetpack Compose/Fragments/Activities)
- [ ] Implement navigation patterns for large screen Android devices (Navigation Drawer, Bottom Navigation)
- [ ] Provide visual feedback for progress, compliance, and logs

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "12. Authentication and Profiles",
        "body": """## Overview
Implement author authentication and multi-user profile scoping.

## Tasks
- [ ] Implement author authentication if supporting multi-user
- [ ] Scope all user data and manuscripts to authenticated profile

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
    {
        "title": "13. Tests and QA",
        "body": """## Overview
Write comprehensive unit, integration, and instrumentation tests for all feature modules.

## Tasks
- [ ] Write unit and integration tests for each feature module
- [ ] Set up instrumentation tests for import/export and editor flows

## References
See [readme.md_app_architecture_backend_logic.md](https://github.com/TronsGuitar/EbookEditor/blob/main/readme.md_app_architecture_backend_logic.md) for full architecture details.

_Part of the [Project Implementation Checklist](../../issues/1)_""",
        "labels": ["enhancement"],
    },
]


def create_issues(token: str, repo: str) -> None:
    api_url = f"https://api.github.com/repos/{repo}/issues"
    headers = {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github.v3+json",
    }

    created = []
    for issue in ISSUES:
        resp = None
        for attempt in range(1, MAX_RETRIES + 1):
            try:
                resp = requests.post(
                    api_url,
                    headers=headers,
                    json=issue,
                    timeout=REQUEST_TIMEOUT_SECONDS,
                )
            except requests.RequestException as exc:
                if attempt == MAX_RETRIES:
                    print(f"❌  Failed to create '{issue['title']}': network error – {exc}")
                    break
                time.sleep(_retry_backoff_seconds(attempt))
                continue

            if resp.status_code in (429, 500, 502, 503, 504):
                if attempt == MAX_RETRIES:
                    print(
                        f"❌  Failed to create '{issue['title']}': "
                        f"HTTP {resp.status_code} after {MAX_RETRIES} attempts – {resp.text[:200]}"
                    )
                    break
                retry_after = _retry_after_seconds(
                    retry_after_value=resp.headers.get("Retry-After"),
                    fallback_seconds=_retry_backoff_seconds(attempt),
                )
                time.sleep(max(retry_after, MIN_RETRY_DELAY_SECONDS))
                continue
            break

        if resp is None:
            continue

        if resp.status_code == 201:
            data = resp.json()
            print(f"✅  Created #{data['number']}: {data['title']}  →  {data['html_url']}")
            created.append(data)
        else:
            print(
                f"❌  Failed to create '{issue['title']}': "
                f"HTTP {resp.status_code} – {resp.text[:200]}"
            )
        # Respect GitHub's secondary rate-limit (1 write/s is safe)
        time.sleep(1)

    print(f"\nDone. Created {len(created)}/{len(ISSUES)} issues.")


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Create eBookEditor checklist issues on GitHub."
    )
    parser.add_argument(
        "--token",
        required=True,
        help="GitHub personal access token (needs 'repo' scope for classic tokens, or 'issues:write' permission for fine-grained tokens)",
    )
    parser.add_argument(
        "--repo",
        default="TronsGuitar/EbookEditor",
        help="owner/repo (default: TronsGuitar/EbookEditor)",
    )
    args = parser.parse_args()
    create_issues(args.token, args.repo)


if __name__ == "__main__":
    main()
