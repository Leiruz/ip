# AI Assistance Log (A-AiAssisted)

Tool used: ChatGPT

How it helped:
- Diagnosed and fixed GitHub Actions CI (Checkstyle config not tracked, .gitignore rules).
- Suggested commands and workflows for tagging/PRs and verifying increments.
- Helped design/implement the C-Sort extension (sorting rules, method extraction, testing).

How I verified AI-assisted changes:
- ./gradlew clean build
- ./gradlew clean test
- ./gradlew checkstyleMain checkstyleTest
- Manual smoke tests (run app and try commands)
