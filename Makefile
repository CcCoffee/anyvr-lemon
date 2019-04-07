# Installs submodules and dependencies.
install:
	git submodule update --init --recursive

# Generates java file from the protobuf-spec.
generate:
	cd anyvr-protobuf-spec && protoc --java_out=../src/main/java/anyvr/app/lemon/generated spec.proto