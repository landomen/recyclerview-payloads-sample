# RecyclerView Payloads Sample
Sample project to show how to use Payloads with `RecyclerView` to achieve more efficient item updates.

Main screen shows a list of hardcoded articles. Each article displays a title, subtitle, image, comments count and a button for the user to bookmark this article.

Additionally, there is a refresh button in the app toolbar to simulate a full refresh of comments count for each article.

There are two `RecyclerView` adapters, both of which use `DiffUtil` to dispatch updates:
* `ArticlesRecyclerViewAdapterWithoutPayload` which doesn't use payloads and does a full re-bind when item changes.
* `ArticlesRecyclerViewAdapterWithPayload` which uses payloads and only updates the views whose properties have changed.

Selecting which adapter to use can be done in `MainActivity.setupRecyclerView()` function.

Result without using payloads (`ArticlesRecyclerViewAdapterWithoutPayload`) on the left and with using payloads (`ArticlesRecyclerViewAdapterWithoutPayload`) on the right:

![recyclerview_nopayload_320_short](https://user-images.githubusercontent.com/16841324/210973587-a5e7621e-9f80-47a3-9583-18bd7edf269e.gif) ![recyclerview_payload_320](https://user-images.githubusercontent.com/16841324/210973627-1b2b7ac7-79ca-4532-97e8-3491bd16837d.gif)
