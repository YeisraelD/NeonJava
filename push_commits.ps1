$branch = (git branch --show-current).Trim()
$commits = git rev-list --reverse "origin/$branch..HEAD"
foreach ($c in $commits) {
    Write-Host "Pushing commit: $c"
    git push origin "$($c):$branch"
    Start-Sleep -Seconds 2
}
Write-Host "All pending commits have been pushed!"
