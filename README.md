# TTL Builder
TTL Builder is an extension to [majavat](https://github.com/ruroru/majavat) that allows scheduling cache reloading. 

## installation

```clojure
[org.clojars.jj/majavat-ttl-builder "1.0.2"]
```

## Usage

Either use builder function

```clojure
(:require [jj.majavat.ttl.builder :as ttl])

(ttl/build-ttl-renderer file-path {:ttl  1
                                   :type TimeUnit/SECONDS})
```

or use builder from `jj.majavat.builder`

```clojure
(:require [jj.majavat.builder :as builder])

(builder/build-renderer (TTLBuilder. {:ttl      ttl
                                      :executor executor
                                      :type     type})
                        file-path
                        template-resolver
                        renderer)
```

## Supported options

| Option   | Default value               |
|----------|-----------------------------|
| ttl      | 7                           | 
| executor | ScheduledThreadPoolExecutor | 
| type     | TimeUnit/DAYS               | 

## License

Copyright © 2025 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
